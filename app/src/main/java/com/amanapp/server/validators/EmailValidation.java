package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class EmailValidation extends Validation {

    public EmailValidation() {
        super();
    }

    @Override
    protected void setValidators() {
        validators = new Validator[]{
                new EmptyStringValidator(), new EmailPatternValidator()
        };
    }

}
