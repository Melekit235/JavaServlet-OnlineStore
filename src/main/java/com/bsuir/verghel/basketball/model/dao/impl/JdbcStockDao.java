package com.bsuir.verghel.basketball.model.dao.impl;

import com.bsuir.verghel.basketball.model.dao.StockDao;
import com.bsuir.verghel.basketball.model.entities.stock.Stock;
import com.bsuir.verghel.basketball.model.entities.stock.StocksExtractor;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;
import com.bsuir.verghel.basketball.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with stock
 *
 * @author Melekit
 * @version 1.0
 */
public class JdbcStockDao implements StockDao {
    /**
     * Instance of logger
     */
    private static final Logger log = Logger.getLogger(StockDao.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * Stock extractor
     */
    private StocksExtractor stocksExtractor = new StocksExtractor();
    /**
     * Instance of connection pool
     */
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    /**
     * Instance of StockDao
     */
    private static volatile StockDao instance;
    /**
     * SQL query to find stock by id
     */
    private static final String GET_STOCK_BY_ID = "SELECT * FROM stocks WHERE ballId = ?";
    /**
     * SQL query to update reserved stock
     */
    private static final String UPDATE_STOCK = "UPDATE stocks SET reserved = ? WHERE ballId = ?";

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of StockDao
     */
    public static StockDao getInstance() {
        if (instance == null) {
            synchronized (StockDao.class) {
                if (instance == null) {
                    instance = new JdbcStockDao();
                }
            }
        }
        return instance;
    }

    /**
     * Find available stock in database
     *
     * @param ballId id of ball
     * @return available stock
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Integer availableStock(Long ballId) throws DaoException {
        Stock stock = getStock(ballId);
        if (stock != null) {
            return stock.getStock() - stock.getReserved();
        } else {
            return 0;
        }
    }

    /**
     * Update reserve number of balls in database
     *
     * @param ballId  - ball to update
     * @param quantity - quantity to add in reserve field
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public void reserve(Long ballId, int quantity) throws DaoException {
        Stock stock = getStock(ballId);
        if (stock != null) {
            int newReserved = stock.getReserved() + quantity;
            Connection conn = null;
            PreparedStatement statement = null;
            try {
                lock.writeLock().lock();
                conn = connectionPool.getConnection();
                statement = conn.prepareStatement(UPDATE_STOCK);
                statement.setLong(2, ballId);
                statement.setLong(1, newReserved);
                statement.execute();
                log.log(Level.INFO, "Update reserve stock in the database");
            } catch (SQLException ex) {
                log.log(Level.ERROR, "Error in reserve", ex);
                throw new DaoException("Error in process of reserving stock");
            } finally {
                lock.writeLock().unlock();
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        log.log(Level.ERROR, "Error in closing statement", ex);
                    }
                }
                if (conn != null) {
                    connectionPool.releaseConnection(conn);
                }
            }
        }
    }

    /**
     * Get stock of ball in database
     *
     * @param ballId id of ball
     * @return stock of ball
     * @throws DaoException throws when there is some errors during dao method execution
     */
    private Stock getStock(Long ballId) throws DaoException {
        Stock stock = null;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_STOCK_BY_ID);
            statement.setLong(1, ballId);
            ResultSet resultSet = statement.executeQuery();
            stock = stocksExtractor.extractData(resultSet).get(0);
            log.log(Level.INFO, "Found stock by ballId in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in getStock", ex);
            throw new DaoException("Error in process of getting stock");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return stock;
    }
}
