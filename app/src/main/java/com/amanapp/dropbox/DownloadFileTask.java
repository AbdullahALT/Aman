package com.amanapp.dropbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amanapp.logics.FileSerialized;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Task to download a file from Dropbox and put it in the Downloads folder
 */
public class DownloadFileTask extends Task<FileSerialized, Void, File> {

    private final Context mContext;
    private String TAG = DownloadFileTask.class.getName();

    public DownloadFileTask(@NonNull Context context, @NonNull DbxClientV2 dbxClient, @NonNull Callback<File> callback) {
        super(dbxClient, callback);
        mContext = context;
    }

    @Override
    protected File doInBackground(FileSerialized... params) {
        FileSerialized metadata = params[0];
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, metadata.getFullName());

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                Log.v(TAG, "path doesn't exist");
                if (!path.mkdirs()) {
                    Log.v(TAG, "Unable to create directory:" + path);
                    exception = new RuntimeException("Unable to create directory: " + path);
                }
                Log.v(TAG, "directory created");
            } else if (!path.isDirectory()) {
                Log.v(TAG, "Download path is not a directory: " + path);
                exception = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                dropboxClient.files().download(metadata.getPathLower(), metadata.getRev())
                        .download(outputStream);
            }
            Log.v(TAG, "File Downloaded on " + file.getPath());
            //Decrypt
            Enigma enigma = new Enigma(file.getAbsolutePath());
            enigma.decrypt();

            // Tell android about the file
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            mContext.sendBroadcast(intent);

            // Tell android about the encrypted file
            Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent2.setData(Uri.fromFile(file));
            mContext.sendBroadcast(intent2);
            Log.v(TAG, "Success");
            return file;
        } catch (DbxException | IOException e) {
            exception = e;
        }
        Log.v(TAG, "An error has accord" + exception.getMessage());
        return null;
    }
}
