package com.amanapp.crypto;

import android.util.Log;

import com.amanapp.dropbox.UploadFileTask;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Abdullah ALT on 12/9/2016.
 */

public class FileCrypto {

    private Cipher cipher;

    public FileCrypto() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Log.d(UploadFileTask.TAG, "File Crypto Constructor");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Log.d(UploadFileTask.TAG, "File Crypto constructed");
    }

    public File encrypt(File file) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
        Log.d(UploadFileTask.TAG, "File Crypto encrypt - File:" + file.getAbsoluteFile());
        byte[] iv = getIvBytes();
        cipher.init(Cipher.ENCRYPT_MODE, SecretKey.get(), new IvParameterSpec(iv));
        return Cryptography.encrypt(cipher, file, iv);
    }

    public File decrypt(File file) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
//        cipher.init(Cipher.DECRYPT_MODE, SecretKey.get(), ivSpec);
        return Cryptography.decrypt(cipher, file, SecretKey.get());
    }

    private byte[] getIvBytes() throws NoSuchAlgorithmException {
        byte[] ivBytes = new byte[16];
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.nextBytes(ivBytes);
        return ivBytes;
    }

}
