package com.epam.esm.dao;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.RepositoryException;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDAO {

    /**
     * gets certificate by id
     *
     * @param id is the id of certificate that is wished to be gotten
     * @return gift certificate with required id
     */
    GiftCertificate read(long id);

    /**
     * gets all certificates
     *
     * @param page is the number of page
     * @param size is the size of page
     * @return list of gift certificates
     */
    List<GiftCertificate> readAll(int page, int size);

    /**
     * inserts new certificate to database
     *
     * @param giftCertificate is the gift certificate that is wished to be inserted
     */
    void insert(GiftCertificate giftCertificate);

    /**
     * deletes gift certificate
     *
     * @param id the id of certificate that will be deleted
     * @throws RepositoryException is the module exception
     */
    void delete(long id) throws RepositoryException;

    /**
     * updates gift certificate
     *
     * @param id              the id of gift certificate that needs to be updated
     * @param giftCertificate is the object that contains new data
     * @throws RepositoryException is the module exception
     */
    void update(long id, GiftCertificate giftCertificate) throws RepositoryException;

    /**
     * gets certificates according to params that was inputted by the user
     *
     * @param mapOfSearchParams is the map of params
     * @param page              is the page
     * @param size              is the size of page
     * @return list of certificates that fit the input params
     */
    List<GiftCertificate> getCertificatesListAccordingToInputParams(Map<String, String> mapOfSearchParams, int page, int size);

    /**
     * gets list of certificates by several tags
     *
     * @param tags is the list of tags
     * @param page is the page
     * @param size is the size of page
     * @return list of certificates that have tags which were inputted by the user
     */
    List<GiftCertificate> getCertificatesBySeveralTags(List<Tag> tags, int page, int size);

    /**
     * gets filtered list of certificates
     *
     * @param params the params by which certificates will be filtered
     * @return list of filtered certificates
     */
    List<GiftCertificate> getFilteredCertificateList(MultiValueMap<String, Object> params);

}
