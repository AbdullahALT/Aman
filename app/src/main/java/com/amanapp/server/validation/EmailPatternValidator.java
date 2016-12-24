package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
final class EmailPatternValidator implements Validator {
    @Override
    public boolean validate(String message) {
        return message.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    @Override
    public String getErrorMessage() {
        return "not a valid email address";
    }


}
