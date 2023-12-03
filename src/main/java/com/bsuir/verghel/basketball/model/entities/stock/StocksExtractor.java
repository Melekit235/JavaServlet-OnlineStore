package com.bsuir.verghel.basketball.model.entities.stock;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcBallDao;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StocksExtractor {
    private BallDao ballDao = JdbcBallDao.getInstance();

    public List<Stock> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Stock> stocks = new ArrayList<>();
        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setBall(ballDao.get(resultSet.getLong("BALLID")).orElse(null));
            stock.setStock(resultSet.getInt("STOCK"));
            stock.setReserved(resultSet.getInt("RESERVED"));
            stocks.add(stock);
        }
        return stocks;
    }
}
