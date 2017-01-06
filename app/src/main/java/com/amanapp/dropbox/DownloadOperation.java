package com.amanapp.dropbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.amanapp.application.core.logics.FileSerialized;

import java.io.File;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public class DownloadOperation extends PermissionOperation<File> {

    private final String TAG = DownloadOperation.class.getName();

    private final FileSerialized file;

    public DownloadOperation(@NonNull Context context, @NonNull FileSerialized file, String waitingMessage, Callback<File> callback) {
        super(context, PermissionsManager.Permission.DOWNLOAD, waitingMessage, callback);
        this.file = file;
    }

    @Override
    protected void startAction() {
        new DownloadFileTask(
                context,
                DropboxClient.getClient(),
                this
        ).execute(file);
        Log.v(TAG, "Start the Download File Task");
    }

    @Override
    public void onPermissionDenied(String permission) {
        Toast.makeText(context,
                "Can't download file: write access denied. " +
                        "Please grant storage permissions to use this functionality.",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        performAction();
    }
}
