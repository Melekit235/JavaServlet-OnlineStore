package com.bsuir.verghel.basketball.model.dao;

import com.bsuir.verghel.basketball.model.exceptions.DaoException;

/**
 * @author Melekit
 * @version 1.0
 */
public interface StockDao {
    /**
     * Find available stock of ball
     *
     * @param ballId id of ball
     * @return available stock
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Integer availableStock(Long ballId) throws DaoException;

    /**
     * Update reserve of balls in database
     *
     * @param ballId  - ball to update
     * @param quantity - quantity to add in reserve field
     * @throws DaoException throws when there is some errors during dao method execution
     */
    void reserve(Long ballId, int quantity) throws DaoException;
}
