package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
import com.epam.esm.dateiniso.DateGenerator;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
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

import javax.persistence.NoResultException;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAOImpl giftCertificateDAO;
    private final Mapper mapper;
    private final DateGenerator dateGenerator;
    private final TagDAO tagDAO;

    @Autowired
    public GiftCertificateServiceImpl(Mapper mapper, GiftCertificateDAOImpl giftCertificateDAO, DateGenerator dateGenerator, TagDAO tagDAO) {
        this.mapper = mapper;
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDAO = tagDAO;
        this.dateGenerator = dateGenerator;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertGiftCertificate(GiftCertificateDTO giftCertificateDTO) {
        giftCertificateDAO.insert(mapper.mapToGiftCertificate(giftCertificateDTO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GiftCertificateDTO getGiftCertificateByID(long id, Locale locale) throws NoSuchIdException {
        GiftCertificate giftCertificate = giftCertificateDAO.read(id).orElseThrow(() -> new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale));
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
        GiftCertificate giftCertificate = mapper.mapToGiftCertificate(giftCertificateDTO);
        try {
            GiftCertificate existingCertificate = giftCertificateDAO.read(id).orElseThrow(NoResultException::new);
            giftCertificateDAO.detachCertificate(existingCertificate);
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
                    if (tagDAO.getTagCountByName(tag).equals(0L)) {
                        tags.add(tag);
                        tagDAO.createTag(tag);
                    } else {
                        Optional<Tag> optionalExistingTag = tagDAO.getTagByName(tag.getName());
                        if (optionalExistingTag.isPresent()){
                            Tag existingTag = optionalExistingTag.get();
                            tags.add(existingTag);
                            tagDAO.updateTag(existingTag);
                        }
                    }
                }
                giftCertificate.setTags(tags);
            }
            giftCertificate.setId(id);
            giftCertificateDAO.update(id, giftCertificate);
        } catch (RepositoryException | NoResultException e) {
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
