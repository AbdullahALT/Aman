package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
interface Validator {
    boolean validate(String message);

    String getErrorMessage();
}
