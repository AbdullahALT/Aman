package com.amanapp.dropbox;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amanapp.application.core.util.UriHelpers;
import com.amanapp.crypto.Enigma;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTask extends Task<String, FileMetadata> {

    public static String TAG = UploadFileTask.class.getName();
    private final Context context;


    public UploadFileTask(Context context, DbxClientV2 dropboxClient, Callback<FileMetadata> callback) {
        super(dropboxClient, callback);
        this.context = context;
    }

    @Override
    protected FileMetadata doInBackground(String... params) {
        String remoteFolderPath = params[0];
        String remoteFileName = params[1];
        String localUri = params[2];
        File localFile = UriHelpers.getFileForUri(context, Uri.parse(localUri));
        if (localFile != null) {
            String name = localFile.getName();
            String parts[] = name.split("\\.");
            String extension = parts[1];
            Log.d(TAG, "localFile Path: " + remoteFolderPath + "/" + remoteFileName + "." + extension);

            File encryptedFile = encryptFile(localFile);
            Log.d(TAG, "encryptedFile is null? " + (encryptedFile == null));
            if (encryptedFile != null) {
                try (InputStream inputStream = new FileInputStream(encryptedFile)) {
                    FileMetadata result = dropboxClient.files().uploadBuilder(remoteFolderPath + "/" + remoteFileName + "." + extension + ".aman")
                            .withMode(WriteMode.OVERWRITE)
                            .uploadAndFinish(inputStream);
                    boolean isDeleted = encryptedFile.delete();
                    Log.d(TAG, "encryptedFile.delete()" + isDeleted);
                    return result;
                } catch (DbxException | IOException e) {
                    exception = e;
                    return null;
                }

            }
        }
        return null;
    }

    private File encryptFile(File file) {
        //            //Encrypt here//
        try {
            Enigma enigma = new Enigma(file.getAbsolutePath());
            Log.d(TAG, "Enigma Created");
            return enigma.encrypt();
        } catch (GeneralSecurityException | IOException e) {
            exception = e;
            return null;
        }
//            //Encrypt end here//
    }
}
