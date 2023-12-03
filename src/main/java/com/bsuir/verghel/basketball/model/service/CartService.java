package com.bsuir.verghel.basketball.model.service;

import com.bsuir.verghel.basketball.model.entities.cart.Cart;
import com.bsuir.verghel.basketball.model.exceptions.OutOfStockException;
import com.bsuir.verghel.basketball.model.exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;

/**
 * @author Melekit
 * @version 1.0
 */
public interface CartService {
    /**
     * Get cart from request
     * @param currentSession session with cart
     * @return  cart
     */
    Cart getCart(HttpSession currentSession);

    /**
     *
     * @param cart cart to add
     * @param productId id of product to add
     * @param quantity quantity of product to add
     * @param currentSession session with cart
     * @throws OutOfStockException throws when ball out of stock when adding
     */
    void add(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException;

    /**
     * Update ball in cart
     * @param cart cart to update
     * @param productId id of ball to update
     * @param quantity quantity of ball to update
     * @param currentSession session with cart
     * @throws OutOfStockException throws when ball out of stock when updating
     */

    void update(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException;

    /**
     * Delete ball from cart
     * @param cart cart to delete
     * @param productId id of ball to delete
     * @param currentSession session with cart
     */

    void delete(Cart cart, Long productId, HttpSession currentSession);

    /**
     * Recalculate cart total price
     * @param cartToRecalculate cat to recalculate
     */

    void reCalculateCart(Cart cartToRecalculate);

    /**
     * Clear cart
     * @param currentSession session with cart
     */
    void clear(HttpSession currentSession);

    /**
     * Remove item from cart
     * @param currentSession session with cart
     * @param ballId id of ball to remove
     */

    void remove(HttpSession currentSession, Long ballId);

}
