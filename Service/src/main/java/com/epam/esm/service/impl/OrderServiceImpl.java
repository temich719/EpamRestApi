package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dtos.DateAndTimeOrderDTO;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final Mapper mapper;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, Mapper mapper) {
        this.orderDAO = orderDAO;
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DateAndTimeOrderDTO getOrderDateAndTimeOfPurchase(long id) {
        return mapper.mapToDateTimeOrderDTO(orderDAO.getOrderDateAndTimeOfPurchase(id));
    }
}
