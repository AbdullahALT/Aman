package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
final class NumberValidator implements Validator {
    @Override
    public boolean validate(String message) {
        return message.matches(".*[0-9].*");
    }

    @Override
    public String getErrorMessage() {
        return "should contain at least one number";
    }


}
