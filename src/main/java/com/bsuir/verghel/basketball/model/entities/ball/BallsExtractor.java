package com.bsuir.verghel.basketball.model.entities.ball;

import com.bsuir.verghel.basketball.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BallsExtractor {


    public List<Ball> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Ball> balls = new ArrayList<>();
        while (resultSet.next()) {
            Ball ball = new Ball();
            ball.setId(resultSet.getLong("ID"));
            ball.setBrand(resultSet.getString("BRAND"));
            ball.setModel(resultSet.getString("MODEL"));
            ball.setPrice(resultSet.getBigDecimal("PRICE"));
            ball.setWeight(resultSet.getBigDecimal("WEIGHT"));
            ball.setMaterial(resultSet.getString("MATERIAL"));
            ball.setSize(resultSet.getInt("SIZE"));
            ball.setCircumference(resultSet.getBigDecimal("CIRCUMFERENCE"));
            ball.setImageUrl(resultSet.getString("IMAGEURL"));
            ball.setDescription(resultSet.getString("DESCRIPTION"));
            balls.add(ball);
        }

        return balls;
    }
}
