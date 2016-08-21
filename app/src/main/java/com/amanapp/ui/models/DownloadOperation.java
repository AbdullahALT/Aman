package com.amanapp.ui.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.DownloadFileTask;

import java.io.File;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public class DownloadOperation extends Operation<File> {

    private final String TAG = DownloadOperation.class.getName();

    private final FileSerialized file;

    public DownloadOperation(@NonNull Context context, @NonNull Activity activity, @NonNull FileSerialized file) {
        super(context, activity, FileAction.DOWNLOAD);
        this.file = file;
    }

    @Override
    protected void startAction() {
        new DownloadFileTask(
                context,
                DropboxClientFactory.getClient(),
                this
        ).execute(file);
        Log.v(TAG, "Start the Download File Task");
    }

    @Override
    protected void onPermissionDenied(String permission) {
        Toast.makeText(context,
                "Can't download file: write access denied. " +
                        "Please grant storage permissions to use this functionality.",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onPermissionGranted(String waitingMessage) {
        performAction(action, waitingMessage);
    }

    @Override
    public void onTaskComplete(File result) {
        dialog.dismiss();
        Toast.makeText(context, "The file has been downloaded", Toast.LENGTH_LONG).show();
        Log.v(TAG, "The file is downloaded");
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
        Toast.makeText(context, "Error downloading the file", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Failed Downloading the file");
    }
}
