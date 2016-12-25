package com.amanapp.server.validation;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class RegisterPasswordValidation extends Validation {

    public RegisterPasswordValidation() {
        super();
    }

    @Override
    protected void setValidators() {
        validators = new Validator[]{
                new LengthValidator(8), new CapitalLetterValidator(), new SmallLetterValidator(),
                new NumberValidator(), new BlackListValidator()

        };
    }
}
