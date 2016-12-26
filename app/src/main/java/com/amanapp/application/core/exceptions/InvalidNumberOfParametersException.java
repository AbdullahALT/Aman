package com.amanapp.application.core.exceptions;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
public class InvalidNumberOfParametersException extends RuntimeException {

    public InvalidNumberOfParametersException() {
        super("The number of parameter is invalid");
    }

    public InvalidNumberOfParametersException(String message) {
        super(message);
    }
}
