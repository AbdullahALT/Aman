package com.amanapp.application.core.exceptions;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
public class InvalidNumberOfQueriesException extends RuntimeException {

    public InvalidNumberOfQueriesException() {
        super("The number of parameter is invalid");
    }

    public InvalidNumberOfQueriesException(String message) {
        super(message);
    }
}
