package com.bsuir.verghel.basketball.model.exceptions;

/**
 * @author Melekit
 * @version 1.0
 * Exception in layer of DAO
 */
public class DaoException extends Exception {
    /**
     * Place message of exception
     *
     * @param message message of exception
     */
    public DaoException(String message) {
        super(message);
    }
}
