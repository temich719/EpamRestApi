package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.domain.Tag;
import com.epam.esm.dtos.TagDTO;
import com.epam.esm.exception.*;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;
    private final Mapper mapper;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, Mapper mapper) {
        this.tagDAO = tagDAO;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTag(TagDTO tagDTO, Locale locale) throws ServiceException {
        try {
            tagDAO.createTag(mapper.mapToTag(tagDTO));
        } catch (RepositoryException e) {
            throw new ServiceException("com.epam.esm.constraint.suchTagIsAlreadyExists", locale);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TagDTO getTagById(long id, Locale locale) throws NoSuchIdException {
        Tag tag = tagDAO.getTagById(id).orElseThrow(() -> new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale));
        return mapper.mapToTagDTO(tag);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TagDTO> getTagList(int page, int size) {
        return mapper.mapToTagDTOList(tagDAO.getTagList(page, size));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(long id, Locale locale) throws NoSuchIdException {
        try {
            tagDAO.deleteTag(id);
        } catch (RepositoryException e) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
    }
}
