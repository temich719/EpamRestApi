package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.*;

import static com.epam.esm.stringsstorage.RepositoryStringsStorage.*;

@Repository
public class TagDAOImpl extends AbstractDAO implements TagDAO {

    private static final String DELETE_TAG_FROM_MANY_TO_MANY_TABLE = "DELETE FROM certificates_and_tags WHERE tag_id = ?;";

    @Autowired
    public TagDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void createTag(Tag tag) throws RepositoryException {
        try {
            getTagByName(tag.getName());
            throw new RepositoryException("Such tag is already exists");
        } catch (NoResultException e) {
            sessionFactory.getCurrentSession().save(tag);
       }
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Tag.class, id));
    }

    @Override
    public List<Tag> getTagList(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        Query<Tag> query = session.createQuery(criteriaQuery);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void deleteTag(long id) throws RepositoryException {
        Session session = sessionFactory.getCurrentSession();
        Tag tag = getTagById(id).orElseThrow(() -> new RepositoryException("There is no such id"));
        session.createNativeQuery(DELETE_TAG_FROM_MANY_TO_MANY_TABLE.replace("?", String.valueOf(tag.getId()))).executeUpdate();
        session.delete(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        sessionFactory.getCurrentSession().update(tag);
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(NAME), parameterExpression));
        Query<Tag> query = session.createQuery(criteriaQuery);
        query.setParameter(parameterExpression, tagName);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Long getTagCountByName(Tag tag) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.equal(root.get(NAME), tag.getName()));
        return sessionFactory.createEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

}
