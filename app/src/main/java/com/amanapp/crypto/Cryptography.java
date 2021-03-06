package com.amanapp.crypto;

import android.util.Log;

import com.amanapp.dropbox.UploadFileTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class Cryptography {

    static File encrypt(Cipher cipher, File file, byte[] iv) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Log.d(UploadFileTask.TAG, "Cryptography encrypt - File:" + file.getAbsoluteFile() + ", cipher is null? " + (cipher == null));
        File outFile = new File(file.getAbsolutePath() + ".aman");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write(iv);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fis.close();
        Log.d(UploadFileTask.TAG, "Cryptography encrypt end - outFile:" + outFile.getAbsoluteFile());
        return outFile;
    }

    static File decrypt(Cipher cipher, File file, SecretKeySpec key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        File outFile = new File(file.getAbsolutePath().replace(".aman", ""));
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] iv = new byte[16];
        fis.read(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        return outFile;
    }
}
