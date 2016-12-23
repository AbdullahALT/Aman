package com.amanapp.server.validators;

import com.amanapp.server.exceptions.ForcedConstantParameterException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
public abstract class Validation {

    protected boolean isValid;
    protected List<String> errorMessages;
    protected Validator[] validators;

    /**
     * this class start validating immediately after calling, so be sure to only initialize when you want to start validating
     *
     */
    public Validation() {
        this.errorMessages = new LinkedList<>();
        setValidators();
    }

    public static Validation Factory(ValidationType type) {
        switch (type) {
            case EMAIL:
                return new EmailValidation();
            case PASSWORD:
                return new PasswordValidation();
            default:
                throw new ForcedConstantParameterException("Validation's Factory accepts only its public static constants or their " +
                        "corresponding values");
        }
    }

    public boolean validate(String message) {
        isValid = true;
        for (Validator validator : validators) {
            if (!validator.validate(message.trim())) {
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
