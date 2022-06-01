package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dateiniso.DateGenerator;
import com.epam.esm.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.stringsstorage.RepositoryStringsStorage.*;

@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    private final static String SELECT_USERS_WITH_HIGHEST_ORDER_COST = "select user_id, userName, sum from (select userName, user_id, sum(cost)\n" +
            "            as sum from user join users_orders uo on user.id = uo.user_id join orders o on o.id = uo.order_id\n" +
            "            group by user_id) as sumTable where sum = (select max(sum) from(select userName, user_id, sum(cost)\n" +
            "            as sum from user join users_orders uo on user.id = uo.user_id join orders o on o.id = uo.order_id\n" +
            "            group by user_id) as zal)";
    private final static String SELECT_MOST_WIDELY_USED_TAGS = "select id, name, creationEntityDate, modificationEntityDate, createdEntityBy, modifiedEntityBy from (select id, name,creationEntityDate, modificationEntityDate, createdEntityBy, modifiedEntityBy, count(name)\n" +
            "            as com from (select * from users_orders where user_id = ?)as users_orders\n" +
            "                      join orders_certificates oc on users_orders.order_id = oc.order_id\n" +
            "                      join certificates_and_tags cat on certificate_id = gift_certificate_id\n" +
            "                      join tag t on cat.tag_id = t.id group by id)as countTable\n" +
            "                      where com = (select max(com)\n" +
            "                      from (select id, name, count(name) from (select * from users_orders where user_id = ?)as users_orders\n" +
            "                                         join orders_certificates oc on users_orders.order_id = oc.order_id\n" +
            "                                         join certificates_and_tags cat on certificate_id = gift_certificate_id\n" +
            "                                         join tag t on cat.tag_id = t.id group by id) as second);";

    private final DateGenerator dateGenerator;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory, DateGenerator dateGenerator) {
        super(sessionFactory);
        this.dateGenerator = dateGenerator;
    }

    @Override
    public List<Order> getUsersOrders(String userName) {
        return getUserByName(userName).getOrders();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(User.class, id));
    }

    @Override
    public List<User> getUsers(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        Query<User> query = session.createQuery(criteriaQuery);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void makeOrder(Order order, Long[] certificateIds) {
        Session session = sessionFactory.getCurrentSession();
        order.setDate(dateGenerator.getCurrentDateAsISO());
        List<GiftCertificate> giftCertificates = getCertificatesByIds(certificateIds);
        order.setCost(String.valueOf(giftCertificates.stream().mapToInt(certificate -> Integer.parseInt(certificate.getPrice().replaceAll("\\$", ""))).sum()));
        order.setGiftCertificates(giftCertificates);

        User user = order.getUser();
        List<Order> orders = new ArrayList<>();
        if (getUsersCountByName(user.getUserName()).equals(0L)){
            session.save(order);
            orders.add(order);
            user.setOrders(orders);
            session.save(user);
        } else {
            User existingUser = getUserByName(user.getUserName());
            order.setUser(existingUser);
            session.save(order);
            existingUser.getOrders().add(order);
            session.update(existingUser);
        }
    }

    @Override
    public List<User> getUsersWithHighestOrdersCostWithMostWidelyUsedTags(int page, int size) {
        List<User> users = getUsersWithHighestOrdersCost(page, size);
        for (User user:users) {
            List<Tag> tags = getMostWidelyUsedTag(user);
            user.setTags(tags);
        }
        return users;
    }

    private List<User> getUsersWithHighestOrdersCost(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        String limit = " limit " + size;
        String offset = " offset " + (page - 1) * size + ";";
        List<UserWithHighestOrdersCost> usersWithHighestOrdersCost = session.createNativeQuery(SELECT_USERS_WITH_HIGHEST_ORDER_COST + limit + offset, UserWithHighestOrdersCost.class).getResultList();
        List<User> users = new ArrayList<>();

        for (UserWithHighestOrdersCost userWithHighestOrdersCost:usersWithHighestOrdersCost) {
            long id = userWithHighestOrdersCost.getUser_id();
            Optional<User> optionalUser = getUserById(id);
            if (optionalUser.isPresent()){
                User user = optionalUser.get();
                user.setSum(userWithHighestOrdersCost.getSum());
                users.add(user);
            }
        }
        return users;
    }

    private List<Tag> getMostWidelyUsedTag(User user) {
        return sessionFactory.getCurrentSession().createNativeQuery(SELECT_MOST_WIDELY_USED_TAGS.replace("?", String.valueOf(user.getId())), Tag.class).getResultList();
    }

    private List<GiftCertificate> getCertificatesByIds(Long[] certificateIds){
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        ParameterExpression<Long> parameterExpression = criteriaBuilder.parameter(Long.class);

        for (Long id: certificateIds) {
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ID), parameterExpression));
            Query<GiftCertificate> query = session.createQuery(criteriaQuery);
            query.setParameter(parameterExpression, id);
            giftCertificates.add(query.getSingleResult());
        }
        return giftCertificates;
    }

    private Long getUsersCountByName(String userName){
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.equal(root.get(USER_NAME), userName));
        Query<Long> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private User getUserByName(String userName){
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(USER_NAME), userName));
        Query<User> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

}
