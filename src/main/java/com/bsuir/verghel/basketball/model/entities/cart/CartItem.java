package com.bsuir.verghel.basketball.model.entities.cart;

import com.bsuir.verghel.basketball.model.entities.ball.Ball;
import com.bsuir.verghel.basketball.model.exceptions.CloneException;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private Ball ball;
    private int quantity;

    public CartItem(Ball product, int quantity) {
        this.ball = product;
        this.quantity = quantity;
    }

    public Ball getBall() {
        return ball;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setBall(Ball product) {
        this.ball = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "code=" + ball.getId() +
                ", quantity=" + quantity;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneException("Error copying the product " + ball.getId() + "with quantity" + quantity);
        }
    }
}
