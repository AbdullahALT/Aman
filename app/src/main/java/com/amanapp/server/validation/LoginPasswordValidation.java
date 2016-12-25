package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 12/24/2016.
 */

class LoginPasswordValidation extends Validation {
    @Override
    protected void setValidators() {
        validators = new Validator[]{
                new EmptyStringValidator()
        };
    }
}
