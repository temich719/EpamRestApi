package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dtos.GiftCertificateDTO;
import com.epam.esm.dtos.TagDTO;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAOImpl giftCertificateDAO;
    private final Mapper mapper;

    @Autowired
    public GiftCertificateServiceImpl(Mapper mapper, GiftCertificateDAOImpl giftCertificateDAO) {
        this.mapper = mapper;
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertGiftCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDAO.insert(mapper.mapToGiftCertificate(giftCertificateDTO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GiftCertificateDTO getGiftCertificateByID(long id, Locale locale) throws NoSuchIdException {
        GiftCertificate giftCertificate = giftCertificateDAO.read(id);
        if (Objects.isNull(giftCertificate)) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
        return mapper.mapToGiftCertificateDTO(giftCertificate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<GiftCertificateDTO> getGiftCertificatesDTOList(int page, int size) {
        return mapper.mapToGiftCertificateDTOList(giftCertificateDAO.readAll(page, size));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGiftCertificate(long id, Locale locale) throws NoSuchIdException {
        try {
            giftCertificateDAO.delete(id);
        } catch (RepositoryException e) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateGiftCertificate(long id, GiftCertificateDTO giftCertificateDTO, Locale locale) throws NoSuchIdException {
        try {
            giftCertificateDAO.update(id, mapper.mapToGiftCertificate(giftCertificateDTO));
        } catch (RepositoryException e) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<GiftCertificateDTO> getCertificatesDTOListAccordingToInputParams(Map<String, String> mapOfSearchParams, int page, int size) {
        return mapper.mapToGiftCertificateDTOList(giftCertificateDAO.getCertificatesListAccordingToInputParams(mapOfSearchParams, page, size));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<GiftCertificateDTO> getCertificatesBySeveralTags(List<TagDTO> tagDTOs, int page, int size) {
        return mapper.mapToGiftCertificateDTOList(giftCertificateDAO.getCertificatesBySeveralTags(mapper.mapToTagList(tagDTOs), page, size));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<GiftCertificateDTO> getFilteredCertificateList(MultiValueMap<String, Object> params) {
        return mapper.mapToGiftCertificateDTOList(giftCertificateDAO.getFilteredCertificateList(params));
    }
}
