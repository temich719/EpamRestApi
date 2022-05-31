package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dateiniso.DateGenerator;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.sqlbuilder.SQLBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.*;

import static com.epam.esm.stringsstorage.RepositoryStringsStorage.*;

@Repository
public class GiftCertificateDAOImpl extends AbstractDAO implements GiftCertificateDAO {

    private final DateGenerator dateGenerator;
    private final SQLBuilder sqlBuilder;

    @Autowired
    public GiftCertificateDAOImpl(SessionFactory sessionFactory, DateGenerator dateGenerator, SQLBuilder sqlBuilder) {
        super(sessionFactory);
        this.dateGenerator = dateGenerator;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public GiftCertificate read(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(GiftCertificate.class, id);
    }

    @Override
    public List<GiftCertificate> getCertificatesBySeveralTags(List<Tag> tags, int page, int size) {
        String sql = sqlBuilder.createGetCertificatesByTagsSQL(tags, page, size);
        return sessionFactory.getCurrentSession().createNativeQuery(sql, GiftCertificate.class).getResultList();
    }

    @Override
    public List<GiftCertificate> readAll(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> getCertificatesListAccordingToInputParams(Map<String, String> mapOfSearchParams, int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        List<GiftCertificate> giftCertificates = null;
        String tagName = mapOfSearchParams.get(TAG_NAME);
        Tag tag = getTagByName(tagName);
        if (Objects.nonNull(tag)) {
            String sql = sqlBuilder.createSearchSQL(mapOfSearchParams, getCertificateIdsByTag(tag), page, size);
            giftCertificates = session.createNativeQuery(sql, GiftCertificate.class).getResultList();
        }
        return giftCertificates;
    }

    @Override
    public void insert(GiftCertificate giftCertificate) {
        Session session = sessionFactory.getCurrentSession();
        Set<Tag> tags = new HashSet<>();
        for (Tag tag : giftCertificate.getTags()) {
            if (getTagCountByName(tag).equals(0L)) {
                tags.add(tag);
                session.save(tag);
            } else {
                Tag existingTag = getTagByName(tag.getName());
                tags.add(existingTag);
            }
        }
        giftCertificate.setTags(tags);
        String dateAsISO = dateGenerator.getCurrentDateAsISO();
        giftCertificate.setCreateDate(dateAsISO);
        giftCertificate.setLastUpdateDate(dateAsISO);
        session.save(giftCertificate);
    }

    @Override
    public void delete(long id) throws RepositoryException {
        Session session = sessionFactory.getCurrentSession();
        try {
            GiftCertificate giftCertificate = getCertificateById(id);
            session.delete(giftCertificate);
        } catch (NoResultException e) {
            throw new RepositoryException();
        }
    }

    @Override
    public void update(long id, GiftCertificate giftCertificate) throws RepositoryException {
        Session session = sessionFactory.getCurrentSession();
        GiftCertificate existingCertificate;
        try {
            existingCertificate = getCertificateById(id);
        } catch (NoResultException e) {
            throw new RepositoryException();
        }
        session.detach(existingCertificate);
        if (Objects.isNull(giftCertificate.getName())) {
            giftCertificate.setName(existingCertificate.getName());
        }
        if (Objects.isNull(giftCertificate.getDescription())) {
            giftCertificate.setDescription(existingCertificate.getDescription());
        }
        if (Objects.isNull(giftCertificate.getPrice())) {
            giftCertificate.setPrice(existingCertificate.getPrice());
        }
        if (Objects.isNull(giftCertificate.getDuration())) {
            giftCertificate.setDuration(existingCertificate.getDuration());
        }
        giftCertificate.setCreateDate(existingCertificate.getCreateDate());
        giftCertificate.setLastUpdateDate(dateGenerator.getCurrentDateAsISO());
        Set<Tag> tags = new HashSet<>();
        if (Objects.isNull(giftCertificate.getTags()) || giftCertificate.getTags().isEmpty()) {
            giftCertificate.setTags(existingCertificate.getTags());
        } else {
            for (Tag tag : giftCertificate.getTags()) {
                if (getTagCountByName(tag).equals(0L)) {
                    tags.add(tag);
                    session.save(tag);
                } else {
                    Tag existingTag = getTagByName(tag.getName());
                    tags.add(existingTag);
                    session.update(existingTag);
                }
            }
            giftCertificate.setTags(tags);
        }
        giftCertificate.setId(id);
        session.update(giftCertificate);
    }

    @Override
    public List<GiftCertificate> getFilteredCertificateList(MultiValueMap<String, Object> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        for (String key : params.keySet()) {
            if (!key.equals(PAGE) && !key.equals(SIZE)) {
                criteriaQuery.where(criteriaBuilder.equal(root.get(key), params.getFirst(key)));
            }
        }
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        if (params.containsKey(PAGE)) {
            int size = Integer.parseInt(String.valueOf(params.getFirst(SIZE)));
            int firstResult = (Integer.parseInt(String.valueOf(params.getFirst(PAGE))) - 1) * size;
            query.setFirstResult(firstResult);
            query.setMaxResults(size);
        }
        return query.getResultList();
    }

    private Long getTagCountByName(Tag tag) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.equal(root.get(NAME), tag.getName()));
        return sessionFactory.createEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

    private Tag getTagByName(String tagName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(NAME), parameterExpression));
        Query<Tag> query = session.createQuery(criteriaQuery);
        query.setParameter(parameterExpression, tagName);
        return query.getSingleResult();
    }

    private GiftCertificate getCertificateById(long id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        ParameterExpression<Long> parameterExpression = criteriaBuilder.parameter(Long.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ID), parameterExpression));
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        query.setParameter(parameterExpression, id);
        return query.getSingleResult();
    }

    private List<Long> getCertificateIdsByTag(Tag tag) {
        List<Long> giftCertificatesIds = new ArrayList<>();
        for (GiftCertificate giftCertificate : tag.getGiftCertificates()) {
            giftCertificatesIds.add(giftCertificate.getId());
        }
        return giftCertificatesIds;
    }

}
