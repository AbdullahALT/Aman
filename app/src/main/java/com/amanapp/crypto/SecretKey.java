package com.amanapp.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Abdullah ALT on 12/9/2016.
 */
public class SecretKey {

    private static SecretKeySpec secretKeySpec = null;

    private SecretKey() {
    }

    public static void init(String password, String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        secretKeySpec = generate(password, salt);
    }

    public static SecretKeySpec get() {
        return secretKeySpec;
    }

    private static SecretKeySpec generate(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 128);
        javax.crypto.SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }


}
