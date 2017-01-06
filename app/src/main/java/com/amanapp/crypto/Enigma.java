package com.amanapp.crypto;

import android.util.Log;

import com.amanapp.dropbox.DownloadFileTask;
import com.amanapp.dropbox.UploadFileTask;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * Created by Abdullah ALT on 12/9/2016.
 */

public class Enigma {

    private String path;
    private FileCrypto crypto;

    public Enigma(String path) {
        this.path = path;
    }

    public File encrypt() throws GeneralSecurityException, IOException {
        if (!isEncrypted()) {
                Log.d(UploadFileTask.TAG, "File not encrypted");
            crypto = new FileCrypto();
                return crypto.encrypt(new File(path));
        }
        Log.d(UploadFileTask.TAG, "File is already encrypted");
        return null;
    }

    public File decrypt() throws GeneralSecurityException, IOException {
        if (isEncrypted()) {
                Log.v(DownloadFileTask.TAG, "File is encrypted");
            crypto = new FileCrypto();
                return crypto.decrypt(new File(path));
        }
        Log.v(DownloadFileTask.TAG, "File is not encrypted");
        return null;
    }

    private boolean isEncrypted() {
        return path.contains(".aman");
    }
}
