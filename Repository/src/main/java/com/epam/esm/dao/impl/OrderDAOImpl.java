package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.domain.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import static com.epam.esm.stringsstorage.RepositoryStringsStorage.*;

@Repository
public class OrderDAOImpl extends AbstractDAO implements OrderDAO {

    @Autowired
    public OrderDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Order getOrderDateAndTimeOfPurchase(long id) {
        Session session = sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setDate(getOrderDate(id, session));
        order.setCost(getOrderCost(id, session));
        return order;
    }

    private String getOrderDate(long orderId, Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        ParameterExpression<Long> parameterExpression = criteriaBuilder.parameter(Long.class);
        criteriaQuery.select(root.get(DATE)).where(criteriaBuilder.equal(root.get(ID), parameterExpression));
        Query<String> query = session.createQuery(criteriaQuery);
        query.setParameter(parameterExpression, orderId);
        return query.getSingleResult();
    }

    private String getOrderCost(long orderId, Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        ParameterExpression<Long> parameterExpression = criteriaBuilder.parameter(Long.class);
        criteriaQuery.select(root.get(COST)).where(criteriaBuilder.equal(root.get(ID), parameterExpression));
        Query<String> query = session.createQuery(criteriaQuery);
        query.setParameter(parameterExpression, orderId);
        return query.getSingleResult();
    }

}
