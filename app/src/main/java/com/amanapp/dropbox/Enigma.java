package com.amanapp.dropbox;

import android.util.Log;

import com.amanapp.crypto.FileCrypto;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * Created by Abdullah ALT on 12/9/2016.
 */

class Enigma {

    private String path;

    Enigma(String path) {
        this.path = path;
    }

    File encrypt() throws GeneralSecurityException, IOException {
        if (!isEncrypted()) {
                Log.d(UploadFileTask.TAG, "File not encrypted");
                FileCrypto crypto = new FileCrypto();
                return crypto.encrypt(new File(path));
        }
        Log.d(UploadFileTask.TAG, "File is already encrypted");
        return null;
    }

    File decrypt() throws GeneralSecurityException, IOException {
        if (isEncrypted()) {
                Log.v(DownloadFileTask.TAG, "File is encrypted");
                FileCrypto crypto = new FileCrypto();
                return crypto.decrypt(new File(path));
        }
        Log.v(DownloadFileTask.TAG, "File is not encrypted");
        return null;
    }

    private boolean isEncrypted() {
        return path.contains(".aman");
    }
}
