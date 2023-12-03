package com.bsuir.verghel.basketball.model.exceptions;

import com.bsuir.verghel.basketball.model.entities.ball.Ball;
/**
 * @author Melekit
 * @version 1.0
 */
public class OutOfStockException extends Exception {
    /**
     * Ball that outOfStock
     */
    private Ball ball;
    /**
     * Requested stock of ball
     */
    private int requestedStock;
    /**
     * Available stock of ball
     */
    private int availableStock;

    /**
     * Constructor of exception
     * @param ball ball of exception
     * @param requestedStock requested stock of exception
     * @param availableStock available stock of exceptttion
     */
    public OutOfStockException(Ball ball, int requestedStock, int availableStock) {
        this.ball = ball;
        this.requestedStock = requestedStock;
        this.availableStock = availableStock;
    }

    /**
     * Place exception message
     * @param s exception message
     */
    public OutOfStockException(String s) {
        super(s);
    }

    public Ball getProduct() {
        return ball;
    }

    public int getRequestedStock() {
        return requestedStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

}
