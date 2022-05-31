package com.epam.esm.service;

import com.epam.esm.dtos.AnswerOrderDTO;
import com.epam.esm.dtos.OrderDTO;
import com.epam.esm.dtos.UserDTO;
import com.epam.esm.dtos.UserHighestOrdersCostDTO;
import com.epam.esm.exception.NoSuchIdException;

import java.util.List;
import java.util.Locale;

public interface UserService {

    /**
     * makes order by some user
     *
     * @param orderDTO is the object that contains who made the order and what certificates it includes
     */
    void makeOrder(OrderDTO orderDTO);

    /**
     * gets all users
     *
     * @param page is the number of page
     * @param size is the size of page
     * @return list of users
     */
    List<UserDTO> getUsers(int page, int size);

    /**
     * gets user by id
     *
     * @param id     is the id of user
     * @param locale is the locale that was inputted in http header
     * @return user who has id the same as given
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    UserDTO getUserById(long id, Locale locale) throws NoSuchIdException;

    /**
     * gets orders of definite user
     *
     * @param userName the name of user
     * @return user with orders and their cost in total
     */
    List<AnswerOrderDTO> getUsersOrders(String userName);

    /**
     * gets users with the highest orders cost with most widely used tag
     *
     * @param page   is the number of page
     * @param size   is the size of page
     * @param locale is the locale that was inputted in http header
     * @return list of users with the highest orders cost with most widely used tag
     * @throws NoSuchIdException is the exception that means that given id doesn't exist in database
     */
    List<UserHighestOrdersCostDTO> getUserWithHighestOrdersCostWithMostWidelyUsedTag(int page, int size, Locale locale) throws NoSuchIdException;

}
