package com.bsuir.verghel.basketball.model.entities.order;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcBallDao;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsExtractor {
    public List<OrderItem> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<OrderItem> orderItems = new ArrayList<>();
        BallDao ballDao = JdbcBallDao.getInstance();
        while (resultSet.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBall(ballDao.get(resultSet.getLong("ballId")).orElse(null));
            orderItem.setQuantity(resultSet.getInt("quantity"));
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
