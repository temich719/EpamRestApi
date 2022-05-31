package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dtos.AnswerOrderDTO;
import com.epam.esm.dtos.OrderDTO;
import com.epam.esm.dtos.UserDTO;
import com.epam.esm.dtos.UserHighestOrdersCostDTO;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final Mapper mapper;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, Mapper mapper) {
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makeOrder(OrderDTO orderDTO) {
        userDAO.makeOrder(mapper.mapToOrder(orderDTO), orderDTO.getGiftCertificateIds());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO getUserById(long id, Locale locale) throws NoSuchIdException {
        try {
            return mapper.mapToUserDTO(userDAO.getUserById(id));
        } catch (RepositoryException e) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<AnswerOrderDTO> getUsersOrders(String userName) {
        List<Order> orders = userDAO.getUsersOrders(userName);
        List<AnswerOrderDTO> answerOrderDTOs = new ArrayList<>();
        for (Order order : orders) {
            answerOrderDTOs.add(mapper.mapToAnswerOrderDTO(order));
        }
        return answerOrderDTOs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserHighestOrdersCostDTO> getUserWithHighestOrdersCostWithMostWidelyUsedTag(int page, int size, Locale locale) throws NoSuchIdException {
        try {
            List<UserHighestOrdersCostDTO> userHighestOrdersCostDTOs = new ArrayList<>();
            for (User user : userDAO.getUsersWithHighestOrdersCostWithMostWidelyUsedTags(page, size)) {
                userHighestOrdersCostDTOs.add(mapper.mapToUserHighestOrdersCostDTOFromUser(user));
            }
            return userHighestOrdersCostDTOs;
        } catch (RepositoryException e) {
            throw new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserDTO> getUsers(int page, int size) {
        return mapper.mapToUserDTOList(userDAO.getUsers(page, size));
    }
}
