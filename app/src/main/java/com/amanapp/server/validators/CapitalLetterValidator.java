package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
final class CapitalLetterValidator implements Validator {

    @Override
    public boolean validate(String message) {
        return message.matches(".*[A-Z].*");
    }

    @Override
    public String getErrorMessage() {
        return "should contain at least one capital letter";
    }


}
