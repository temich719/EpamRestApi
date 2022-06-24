package com.epam.esm.service;

import com.epam.esm.dto.DateAndTimeOrderDTO;

public interface OrderService {

    /**
     * gets order with date and time of its purchase
     *
     * @param id is the id of the order
     * @return date and time of purchase
     */
    DateAndTimeOrderDTO getOrderDateAndTimeOfPurchase(long id);

}
