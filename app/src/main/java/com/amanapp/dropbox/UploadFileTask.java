package com.amanapp.dropbox;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amanapp.utilities.UriHelpers;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTask extends Task<String, Integer, FileMetadata> {

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
//            //Encrypt here//
            Enigma enigma = new Enigma(localFile.getAbsolutePath());
            Log.d(TAG, "Enigma Created");
            File encryptedFile = enigma.encrypt();
            Log.d(TAG, "encryptedFile is null? " + (encryptedFile == null));
//            //Encrypt end here//
            if (encryptedFile != null) {
                try (InputStream inputStream = new FileInputStream(encryptedFile)) {
                    return dropboxClient.files().uploadBuilder(remoteFolderPath + "/" + remoteFileName + "." + extension + ".aman")
                            .withMode(WriteMode.OVERWRITE)
                            .uploadAndFinish(inputStream);
                } catch (DbxException | IOException e) {
                    exception = e;
                }
            }
        }
        return null;
    }
}
