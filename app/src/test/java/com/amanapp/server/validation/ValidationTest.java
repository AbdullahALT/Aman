package com.amanapp.server.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Abdullah ALT on 12/23/2016.
 */
public class ValidationTest {

    private Validation emailValidation = Validation.Factory(Validation.ValidationType.EMAIL);
    private Validation passwordValidation = Validation.Factory(Validation.ValidationType.REGISTRATION_PASSWORD);

    @Test
    public void InvalidEmail() throws Exception {
        assertFalse(emailValidation.validate("Hello"));
    }

    @Test
    public void ValidEmail() throws Exception {
        assertTrue(emailValidation.validate("hello@email.com"));
    }

    @Test
    public void ValidEmail2() throws Exception {
        assertTrue(emailValidation.validate("hello@email.com.net"));
    }

    @Test
    public void ValidEmailEmpty() throws Exception {
        EmptyStringValidator empty = new EmptyStringValidator();
        emailValidation.validate("");
        assertEquals(empty.getErrorMessage(), emailValidation.getErrorMessages().get(0));
    }

    @Test
    public void validatePasswordEmpty() throws Exception {
        EmptyStringValidator empty = new EmptyStringValidator();
        passwordValidation.validate("");
        assertEquals(empty.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
    }

    @Test
    public void validatePasswordLength() throws Exception {
        int length = 8;
        LengthValidator lengthValidator = new LengthValidator(length);
        String password = "";
        for (int i = 1; i < 8; i++) {
            password += "i";
            passwordValidation.validate(password);
            assertEquals(lengthValidator.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
        }
    }

    @Test
    public void validatePasswordCapital() throws Exception {
        CapitalLetterValidator validator = new CapitalLetterValidator();
        passwordValidation.validate("test1234");
        assertEquals(validator.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
    }

    @Test
    public void validatePasswordSmall() throws Exception {
        SmallLetterValidator validator = new SmallLetterValidator();
        passwordValidation.validate("TEST1234");
        assertEquals(validator.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
    }

    @Test
    public void validatePasswordNumber() throws Exception {
        NumberValidator validator = new NumberValidator();
        passwordValidation.validate("testTEST");
        assertEquals(validator.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
    }

    @Test
    public void validatePasswordBlackList() throws Exception {
        BlackListValidator validator = new BlackListValidator();
        passwordValidation.validate("Password123");
        assertEquals(validator.getErrorMessage(), passwordValidation.getErrorMessages().get(0));
    }


}