package com.amanapp.server.validators;

import com.amanapp.server.exceptions.ForcedConstantParameterException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
public abstract class Validation {

    protected String message;
    protected boolean isValid;
    protected List<String> errorMessages;
    protected Validator[] validators;

    /**
     * this class start validating immediately after calling, so be sure to only initialize when you want to start validating
     *
     * @param message: the message to be validate
     */
    public Validation(String message) {
        this.message = message.trim();
        this.errorMessages = new LinkedList<>();
        setValidators();
        validate();
    }

    public static Validation Factory(String message, ValidationType type) {
        switch (type) {
            case EMAIL:
                return new EmailValidation(message);
            case PASSWORD:
                return new PasswordValidation(message);
            default:
                throw new ForcedConstantParameterException("Validation's Factory accepts only its public static constants or their " +
                        "corresponding values");
        }
    }

    protected boolean validate() {
        isValid = true;
        for (Validator validator : validators) {
            if (!validator.validate(message)) {
                errorMessages.add(validator.getErrorMessage());
                isValid = false;
            }
        }
        return isValid;
    }

    protected abstract void setValidators();

    public boolean isValid() {
        return isValid;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public enum ValidationType {EMAIL, PASSWORD}
}
