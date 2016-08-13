package com.amanapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.amanapp.tasks.callbacks.Callback;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Task to download a file from Dropbox and put it in the Downloads folder
 */
public class DownloadFileTask extends Task<FileMetadata, Void, File> {

    private final Context mContext;


    public DownloadFileTask(Context context, DbxClientV2 dbxClient, Callback<File> callback) {
        super(dbxClient, callback);
        mContext = context;
    }

    @Override
    protected File doInBackground(FileMetadata... params) {
        FileMetadata metadata = params[0];
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, metadata.getName());

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    exception = new RuntimeException("Unable to create directory: " + path);
                }
            } else if (!path.isDirectory()) {
                exception = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                dropboxClient.files().download(metadata.getPathLower(), metadata.getRev())
                        .download(outputStream);
            }

            // Tell android about the file
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            mContext.sendBroadcast(intent);

            return file;
        } catch (DbxException | IOException e) {
            exception = e;
        }

        return null;
    }
}
