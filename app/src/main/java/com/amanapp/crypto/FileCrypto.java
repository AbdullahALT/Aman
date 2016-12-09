package com.amanapp.crypto;

import android.util.Log;

import com.amanapp.dropbox.UploadFileTask;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Abdullah ALT on 12/9/2016.
 */

public class FileCrypto {

    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private Cipher cipher;
    private IvParameterSpec ivSpec;

    public FileCrypto() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Log.d(UploadFileTask.TAG, "File Crypto Constructor");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ivSpec = new IvParameterSpec(ivBytes);
        Log.d(UploadFileTask.TAG, "File Crypto constructed");
    }

    public File encrypt(File file) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
        Log.d(UploadFileTask.TAG, "File Crypto encrypt - File:" + file.getAbsoluteFile());
        cipher.init(Cipher.ENCRYPT_MODE, SecretKey.get(), ivSpec);
        return Cryptography.encrypt(cipher, file);
    }

    public File decrypt(File file) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.DECRYPT_MODE, SecretKey.get(), ivSpec);
        return Cryptography.decrypt(cipher, file);
    }

}
