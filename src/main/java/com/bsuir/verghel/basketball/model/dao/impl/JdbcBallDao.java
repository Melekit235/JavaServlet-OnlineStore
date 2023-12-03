package com.bsuir.verghel.basketball.model.dao.impl;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.entities.ball.Ball;
import com.bsuir.verghel.basketball.model.entities.ball.BallsExtractor;
import com.bsuir.verghel.basketball.model.enums.SortField;
import com.bsuir.verghel.basketball.model.enums.SortOrder;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;
import com.bsuir.verghel.basketball.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with balls
 *
 * @author Melekit
 * @version 1.0
 */
public class JdbcBallDao implements BallDao {
    /**
     * Instance of logger
     */
    private static final Logger log = Logger.getLogger(BallDao.class);
    /**
     * Instance of ballExtractor
     */
    private BallsExtractor ballsExtractor = new BallsExtractor();
    /**
     * Instance of BallDao
     */
    private static volatile BallDao instance;
    /**
     * Instance of ConnectionPool
     */
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    /**
     * SQL query to find balls by id
     */
    private static final String GET_QUERY = "SELECT * FROM balls WHERE id = ?";
    /**
     * SQL query to find all balls with available stock > 0, limit and offset
     */
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select BALLS.* from BALLS " +
            "left join STOCKS on BALLS.ID = STOCKS.BALLID where STOCKS.STOCK - STOCKS.RESERVED > 0 and balls.price > 0 offset ? limit ?) ph";
    /**
     * SQL query to find all balls with available stock
     */
    private static final String FIND_WITHOUT_OFFSET_AND_LIMIT = "SELECT ph.* " +
            "FROM (SELECT balls.* FROM balls " +
            "LEFT JOIN stocks ON balls.id = stocks.ballID WHERE stocks.stock - stocks.reserved > 0 ";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * SQL query to find number of balls
     */
    private static final String NUMBER_OF_BALLS_QUERY = "SELECT count(*) FROM BALLS LEFT JOIN STOCKS ON BALLS.ID = STOCKS.BALLID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0 AND balls.price > 0";

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of BallDao
     */
    public static BallDao getInstance() {
        if (instance == null) {
            synchronized (BallDao.class) {
                if (instance == null) {
                    instance = new JdbcBallDao();
                }
            }
        }
        return instance;
    }

    /**
     * Get ball by id from database
     *
     * @param key id of ball
     * @return ball
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Optional<Ball> get(Long key) throws DaoException {
        Optional<Ball> ball;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_QUERY);
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            ball = ballsExtractor.extractData(resultSet).stream().findAny();
            log.log(Level.INFO, "Found balls by id in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in get function", ex);
            throw new DaoException("Error in process of getting ball");
        } finally {
            lock.readLock().unlock();
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
        return ball;
    }

    /**
     * Find all balls from database
     *
     * @param offset    - offset of found balls
     * @param limit     - limit of found balls
     * @param sortField - field to sort (model, brand, price, display size)
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return list of balls
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public List<Ball> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException {
        List<Ball> balls;
        String sql = makeFindAllSQL(sortField, sortOrder, query);
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            balls = ballsExtractor.extractData(resultSet);
            log.log(Level.INFO, "Found all balls in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in findAll", ex);
            throw new DaoException("Error in process of getting all balls");
        } finally {
            lock.readLock().unlock();
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
        return balls;
    }

    /**
     * Find number of balls by query from database
     *
     * @param query - query for find
     * @return number of balls
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Long numberByQuery(String query) throws DaoException {
        String sql;
        if (query == null || query.equals("")) {
            sql = NUMBER_OF_BALLS_QUERY;
        } else {
            sql = NUMBER_OF_BALLS_QUERY + " AND " +
                    "(LOWER(BALLS.BRAND) LIKE LOWER('" + query + "%') " +
                    "OR LOWER(BALLS.BRAND) LIKE LOWER('% " + query + "%') " +
                    "OR LOWER(BALLS.MODEL) LIKE LOWER('" + query + "%') " +
                    "OR LOWER(BALLS.MODEL) LIKE LOWER('% " + query + "%'))";
        }
        Connection conn = null;
        Statement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
            log.log(Level.INFO, "Found count of balls");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in numberByQuery", ex);
            throw new DaoException("Error in process of getting number of balls");
        } finally {
            lock.readLock().unlock();
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
        return 0L;
    }

    /**
     * Make sql query with sorting and finding
     *
     * @param sortField - field to sort
     * @param sortOrder - order to sort
     * @param query     - query to find
     * @return sql query
     */
    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            StringBuilder sql = new StringBuilder(FIND_WITHOUT_OFFSET_AND_LIMIT);

            if (query != null && !query.equals("")) {
                sql.append("AND (" + "LOWER(BALLS.BRAND) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(BALLS.BRAND) LIKE LOWER('% ").append(query).append("%') ").
                        append("OR LOWER(BALLS.MODEL) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(BALLS.MODEL) LIKE LOWER('% ").append(query).append("%')").append(") ");
            }
            sql.append("AND BALLS.PRICE > 0 ");
            if (sortField != null) {
                sql.append("ORDER BY ").append(sortField.name()).append(" ");
                if (sortOrder != null) {
                    sql.append(sortOrder.name()).append(" ");
                } else {
                    sql.append("ASC ");
                }
            }
            sql.append("offset ? limit ?) ph");
            return sql.toString();
        } else {
            return SIMPLE_FIND_ALL_QUERY;
        }
    }
}
