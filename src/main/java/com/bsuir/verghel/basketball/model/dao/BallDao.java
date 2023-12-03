package com.bsuir.verghel.basketball.model.dao;

import com.bsuir.verghel.basketball.model.entities.ball.Ball;
import com.bsuir.verghel.basketball.model.enums.SortField;
import com.bsuir.verghel.basketball.model.enums.SortOrder;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * @author Melekit
 * @version 1.0
 */
public interface BallDao {
    /**
     * Find ball by id
     *
     * @param key id of ball
     * @return ball with id
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Optional<Ball> get(Long key) throws DaoException;

    /**
     * Find balls from database
     *
     * @param offset    - offset of found balls
     * @param limit     - limit of found balls
     * @param sortField - field to sort (model, brand, price, display size)
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return List of balls
     * @throws DaoException throws when there is some errors during dao method execution
     */

    List<Ball> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException;

    /**
     * Number of founded balls
     *
     * @param query - query for find
     * @return number of balls
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Long numberByQuery(String query) throws DaoException;
}
