package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.NotInsertedException;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface GiftCertificateService {

    /**
     * inserts GiftCertificate object to gift_certificate table
     *
     * @param giftCertificateDTO is a GiftCertificateDTO object
     * @throws NotInsertedException is the exception that means that object wasn't inserted in database
     */
    void insertGiftCertificate(GiftCertificateDTO giftCertificateDTO) throws NotInsertedException;

    /**
     * finds GiftCertificate in database according to given id
     *
     * @param id     is the GiftCertificate id
     * @param locale is the locale that was in http header
     * @return GiftCertificateDTO object that was made from GiftCertificate object that has the same id as given
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    GiftCertificateDTO getGiftCertificateByID(long id, Locale locale) throws NoSuchIdException;

    /**
     * finds all existing GiftCertificate objects in database
     *
     * @param page is the number of page
     * @param size is the size of page
     * @return list of all GiftCertificate objects mapped to GiftCertificateDTO
     */
    List<GiftCertificateDTO> getGiftCertificatesDTOList(int page, int size);

    /**
     * deletes GiftCertificate according to given id
     *
     * @param id     is the GiftCertificate id
     * @param locale is the locale that was in http header
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    void deleteGiftCertificate(long id, Locale locale) throws NoSuchIdException;

    /**
     * updates GiftCertificate info
     *
     * @param id                 is the id of GiftCertificate that need to be updated
     * @param giftCertificateDTO is the object that contains new data
     * @param locale             is the locale that was in http header
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    void updateGiftCertificate(long id, GiftCertificateDTO giftCertificateDTO, Locale locale) throws NoSuchIdException;

    /**
     * finds GiftCertificate objects according to given params
     *
     * @param mapOfSearchParams is the map of searching params
     * @param page              is the number of page
     * @param size              is the size of page
     * @return list of GiftCertificate objects that are corresponding given params mapped to GiftCertificateDTO
     */
    List<GiftCertificateDTO> getCertificatesDTOListAccordingToInputParams(Map<String, String> mapOfSearchParams, int page, int size);

    /**
     * gets certificates that have tags the same as in given list of tags
     *
     * @param tagDTOs the list of tags
     * @param page    is the number of page
     * @param size    is the size of page
     * @return list of gift certificates DTO
     */
    List<GiftCertificateDTO> getCertificatesBySeveralTags(List<TagDTO> tagDTOs, int page, int size);

    /**
     * gets filtered list of certificates
     *
     * @param params the params by which certificates will be filtered
     * @return filtered list of certificates
     */
    List<GiftCertificateDTO> getFilteredCertificateList(MultiValueMap<String, Object> params);

}
