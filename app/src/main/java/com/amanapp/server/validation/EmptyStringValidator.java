package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 11/20/2016.
 */
final class EmptyStringValidator implements Validator {
    @Override
    public boolean validate(String message) {
        return !message.isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return "This field can't be empty";
    }
}
