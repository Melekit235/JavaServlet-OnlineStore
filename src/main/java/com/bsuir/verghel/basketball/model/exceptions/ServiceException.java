package com.bsuir.verghel.basketball.model.exceptions;

/**
 * @author Melekit
 * @version 1.0
 * Exception in layer of services
 */
public class ServiceException extends Exception {
    /**
     * Place message of exception
     *
     * @param message message of exception
     */
    public ServiceException(String message) {
        super(message);
    }
}
