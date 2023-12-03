package com.bsuir.verghel.basketball.model.exceptions;

/**
 * @author Melekit
 * @version 1.0
 */
public class CloneException extends RuntimeException {
    /**
     * Place message of exception
     * @param message message of exception
     */
    public CloneException(String message) {
        super(message);
    }
}
