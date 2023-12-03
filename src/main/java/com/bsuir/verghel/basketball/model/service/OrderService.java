package com.bsuir.verghel.basketball.model.service;

import com.bsuir.verghel.basketball.model.entities.cart.Cart;
import com.bsuir.verghel.basketball.model.entities.order.Order;
import com.bsuir.verghel.basketball.model.entities.order.OrderStatus;
import com.bsuir.verghel.basketball.model.exceptions.OutOfStockException;
import com.bsuir.verghel.basketball.model.exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

/**
 * @author Melekit
 * @version 1.0
 */
public interface OrderService {
    /**
     * Create empty order
     * @param cart cart with items
     * @return order
     */
    Order createOrder(Cart cart);

    /**
     * Place order in database
     * @param order order to place
     * @param session session with cart
     * @throws OutOfStockException throws when some product out of stock when placing cart
     */
    void placeOrder(Order order, HttpSession session) throws OutOfStockException, ServiceException;

    /**
     * Change order status in database
     * @param id id of order
     * @param status new status of order
     */
    void changeOrderStatus(Long id, OrderStatus status) throws ServiceException;

    Optional<Order> getById (Long id) throws ServiceException;
}
