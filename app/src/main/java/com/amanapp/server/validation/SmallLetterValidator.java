package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
final class SmallLetterValidator implements Validator {
    @Override
    public boolean validate(String message) {
        return message.matches(".*[a-z].*");
    }

    @Override
    public String getErrorMessage() {
        return "should contain at least one small letter";
    }
}
