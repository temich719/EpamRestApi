package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
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

import javax.persistence.criteria.*;
import java.util.*;

import static com.epam.esm.stringsstorage.RepositoryStringsStorage.*;

@Repository
public class GiftCertificateDAOImpl extends AbstractDAO implements GiftCertificateDAO {

    private final static String MAKE_CERTIFICATE_NON_ACTIVE = "update gift_certificate set status = false where id = ?;";

    private final DateGenerator dateGenerator;
    private final SQLBuilder sqlBuilder;
    private final TagDAO tagDAO;

    @Autowired
    public GiftCertificateDAOImpl(SessionFactory sessionFactory, DateGenerator dateGenerator, SQLBuilder sqlBuilder, TagDAO tagDAO) {
        super(sessionFactory);
        this.dateGenerator = dateGenerator;
        this.sqlBuilder = sqlBuilder;
        this.tagDAO = tagDAO;
    }

    @Override
    public Optional<GiftCertificate> read(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(GiftCertificate.class, id));
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
        Optional<Tag> optionalTag = tagDAO.getTagByName(tagName);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
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
            if (tagDAO.getTagCountByName(tag).equals(0L)) {
                tags.add(tag);
                session.save(tag);
            } else {
                Optional<Tag> optionalExistingTag = tagDAO.getTagByName(tag.getName());
                optionalExistingTag.ifPresent(tags::add);
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
        Optional<GiftCertificate> optionalGiftCertificate = read(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            makeCertificateNonActive(giftCertificate);
        } else {
            throw new RepositoryException("There is no such id");
        }
    }

    @Override
    public void update(long id, GiftCertificate giftCertificate) {
        sessionFactory.getCurrentSession().update(giftCertificate);
    }

    @Override
    public void detachCertificate(GiftCertificate giftCertificate) {
        sessionFactory.getCurrentSession().detach(giftCertificate);
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

    private List<Long> getCertificateIdsByTag(Tag tag) {
        List<Long> giftCertificatesIds = new ArrayList<>();
        for (GiftCertificate giftCertificate : tag.getGiftCertificates()) {
            giftCertificatesIds.add(giftCertificate.getId());
        }
        return giftCertificatesIds;
    }

    private void makeCertificateNonActive(GiftCertificate giftCertificate){
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(MAKE_CERTIFICATE_NON_ACTIVE.replaceAll("\\?", String.valueOf(giftCertificate.getId()))).executeUpdate();
    }

}
