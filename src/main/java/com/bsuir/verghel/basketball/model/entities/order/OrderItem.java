package com.bsuir.verghel.basketball.model.entities.order;

import com.bsuir.verghel.basketball.model.entities.ball.Ball;

public class OrderItem {
    private Long id;
    private Ball ball;
    private Order order;
    private int quantity;

    public Ball getBall() {
        return ball;
    }

    public void setBall(final Ball ball) {
        this.ball = ball;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}