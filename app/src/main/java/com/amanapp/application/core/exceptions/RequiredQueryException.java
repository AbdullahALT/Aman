package com.amanapp.application.core.exceptions;

/**
 * Created by Abdullah ALT on 11/16/2016.
 */
public class RequiredQueryException extends RuntimeException {
    public RequiredQueryException() {
        super("One or more required query is missing");
    }

    public RequiredQueryException(String message) {
        super(message);
    }
}
