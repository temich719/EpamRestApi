package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Locale;

public interface TagService {

    /**
     * creates Tag object to tag table
     *
     * @param tagDTO is the TagDTO object that contains data need to be inserted to database
     * @param locale is the locale that was inputted in http header
     * @throws ServiceException is the module exception
     */
    void createTag(TagDTO tagDTO, Locale locale) throws ServiceException;

    /**
     * finds Tag object according to given id
     *
     * @param id     is the Tag object id
     * @param locale is the locale that was inputted in http header
     * @return Tag object that has id the same as given one's mapped to TagDTO
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    TagDTO getTagById(long id, Locale locale) throws NoSuchIdException;

    /**
     * finds all Tag objects
     *
     * @param page is the number of page that will be returned
     * @param size is the size of page
     * @return list of all Tag objects that exist in database mapped to TagDTO
     */
    List<TagDTO> getTagList(int page, int size);

    /**
     * deletes Tag according to given id
     *
     * @param id     is the Tag id
     * @param locale is the locale that was inputted in http header
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    void deleteTag(long id, Locale locale) throws NoSuchIdException;

}
