package com.bsuir.verghel.basketball.model.dao;

import com.bsuir.verghel.basketball.model.entities.order.OrderItem;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;

import java.util.List;

/**
 * @author Melekit
 * @version 1.0
 */
public interface OrderItemDao {
    /**
     * Find items from order
     *
     * @param key key of order
     * @return List of OrderItems from order
     * @throws DaoException throws when there is some errors during dao method execution
     */
    List<OrderItem> getOrderItems(Long key) throws DaoException;
}
