package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface TagDAO {

    /**
     * creates Tag object to tag table
     *
     * @param tag is the Tag object that need to be inserted to database
     * @throws RepositoryException is the module exception
     */
    void createTag(Tag tag) throws RepositoryException;

    /**
     * finds Tag object according to given id
     *
     * @param id is the Tag object id
     * @return Tag object that has id the same as given one's
     */
    Optional<Tag> getTagById(long id);

    /**
     * finds all Tag objects
     *
     * @param page is the number of page
     * @param size is the size of page
     * @return list of all Tag objects that exist in database
     */
    List<Tag> getTagList(int page, int size);

    /**
     * deletes Tag according to given id
     *
     * @param id is the Tag id
     * @throws RepositoryException is the module exception
     */
    void deleteTag(long id) throws RepositoryException;

    /**
     * updates tag
     *
     * @param tag is the new tag
     */
    void updateTag(Tag tag);

    /**
     * gets tag by name
     *
     * @param tagName is the tag name
     * @return tag with given name
     */
    Optional<Tag> getTagByName(String tagName);

    /**
     * gets count of tags that have name the same as given
     *
     * @param tag is the tag with given name
     * @return count of tags
     */
    Long getTagCountByName(Tag tag);

}
