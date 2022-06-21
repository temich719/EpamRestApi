package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dtos.*;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserDAO userDAO;
    private final Mapper mapper;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, Mapper mapper) {
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO getUserByName(String userName) throws ServiceException {
        Optional<User> optionalUser = userDAO.getUserByName(userName);
        if (optionalUser.isPresent()){
            return mapper.mapToUserDTO(optionalUser.get());
        } else {
            throw new ServiceException("com.epam.esm.constraint.userDoesntExist");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(SecurityUserDTO securityUserDTO) throws ServiceException {
        try {
            User user = mapper.mapToUser(securityUserDTO);
            user.setRole(ROLE_USER);
            userDAO.saveUser(user);
        } catch (RepositoryException e){
            throw new ServiceException("com.epam.esm.constraint.userIsAlreadyExist");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makeOrder(OrderDTO orderDTO) {
        userDAO.makeOrder(mapper.mapToOrder(orderDTO), orderDTO.getGiftCertificateIds());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO getUserById(long id, Locale locale) throws NoSuchIdException {
        User user = userDAO.getUserById(id).orElseThrow(() -> new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale));
        return mapper.mapToUserDTO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserDTO getUserById(BigInteger id, Locale locale) throws NoSuchIdException {
        User user = userDAO.getUserById(id).orElseThrow(() -> new NoSuchIdException("com.epam.esm.constraint.noSuchIdException", locale));
        return mapper.mapToUserDTO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<AnswerOrderDTO> getUsersOrders(String userName) throws ServiceException {
        try {
            List<Order> orders = userDAO.getUsersOrders(userName);
            List<AnswerOrderDTO> answerOrderDTOs = new ArrayList<>();
            for (Order order : orders) {
                answerOrderDTOs.add(mapper.mapToAnswerOrderDTO(order));
            }
            return answerOrderDTOs;
        } catch (RepositoryException e){
            throw new ServiceException("com.epam.esm.constraint.userDoesntExist");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserHighestOrdersCostDTO> getUserWithHighestOrdersCostWithMostWidelyUsedTag(int page, int size, Locale locale) {
        List<UserHighestOrdersCostDTO> userHighestOrdersCostDTOs = new ArrayList<>();
        for (User user : userDAO.getUsersWithHighestOrdersCostWithMostWidelyUsedTags(page, size)) {
            userHighestOrdersCostDTOs.add(mapper.mapToUserHighestOrdersCostDTOFromUser(user));
        }
        return userHighestOrdersCostDTOs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserDTO> getUsers(int page, int size) {
        return mapper.mapToUserDTOList(userDAO.getUsers(page, size));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleUser getUserWithRole(String userName) throws ServiceException {
        User user = userDAO.getUserByName(userName).orElseThrow(() -> new ServiceException("com.epam.esm.constraint.userDoesntExist"));
        return mapper.mapToRoleUser(user);
    }
}
