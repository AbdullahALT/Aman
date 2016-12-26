package com.amanapp.application.core.exceptions;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
public class ForcedConstantParameterException extends RuntimeException {

    public ForcedConstantParameterException() {
        super("Factory accepts only its public static constants or their corresponding values");
    }

    public ForcedConstantParameterException(String message) {
        super(message);
    }
}
