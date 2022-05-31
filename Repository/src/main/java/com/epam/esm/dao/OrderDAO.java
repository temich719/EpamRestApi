package com.epam.esm.dao;

import com.epam.esm.domain.Order;

public interface OrderDAO {

    /**
     * gets order date and time of it's purchase
     *
     * @param id is the id of the order
     * @return order
     */
    Order getOrderDateAndTimeOfPurchase(long id);

}
