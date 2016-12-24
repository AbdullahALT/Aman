package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
final class LengthValidator implements Validator {

    private int length;

    public LengthValidator(int length) {
        this.length = length;
    }

    @Override
    public boolean validate(String message) {
        return message.length() >= length;
    }

    @Override
    public String getErrorMessage() {
        return "the length should be " + length + " or more";
    }


}
