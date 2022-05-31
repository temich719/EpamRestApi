package com.epam.esm.dao;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface UserDAO {

    /**
     * gets user by id
     *
     * @param id is the id
     * @return user that has id that equals id param
     * @throws RepositoryException is the module exception
     */
    User getUserById(long id) throws RepositoryException;

    /**
     * get list of users
     *
     * @param page is the page
     * @param size is the size of page
     * @return list of all users
     */
    List<User> getUsers(int page, int size);

    /**
     * makes order
     *
     * @param order          is the order
     * @param certificateIds the ids of certificates that order will contain
     */
    void makeOrder(Order order, Long[] certificateIds);

    /**
     * gets orders of user
     *
     * @param userName the name of the user
     * @return orders of user who has name that equals userName param
     */
    List<Order> getUsersOrders(String userName);

    /**
     * gets users with the highest orders cost and the most widely used tags
     *
     * @param page is the page
     * @param size is the size of page
     * @return list of users
     * @throws RepositoryException is the module exception
     */
    List<User> getUsersWithHighestOrdersCostWithMostWidelyUsedTags(int page, int size) throws RepositoryException;
}
