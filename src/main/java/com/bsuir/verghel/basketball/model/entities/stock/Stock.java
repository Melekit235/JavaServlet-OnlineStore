package com.bsuir.verghel.basketball.model.entities.stock;

import com.bsuir.verghel.basketball.model.entities.ball.Ball;

public class Stock {
    private Ball ball;
    private Integer stock;
    private Integer reserved;

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

}
