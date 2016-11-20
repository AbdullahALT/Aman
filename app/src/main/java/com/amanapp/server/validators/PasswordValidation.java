package com.amanapp.server.validators;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class PasswordValidation extends Validation {

    public PasswordValidation(String message) {
        super(message);
    }

    @Override
    protected void setValidators() {
        validators = new Validator[]{
                new EmptyStringValidator(), new LengthValidator(8), new CapitalLetterValidator(), new SmallLetterValidator(),
                new NumberValidator(), new BlackListValidator()

        };
    }
}
