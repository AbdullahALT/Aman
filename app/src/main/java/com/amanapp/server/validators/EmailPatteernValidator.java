package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class EmailPatteernValidator implements Validator {
    @Override
    public boolean validate(String message) {
        return message.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-][A-Za-z0-9]+\\.[A-Za-z]{2,}$");
    }

    @Override
    public String getErrorMessage() {
        return "not a valid email address";
    }


}
