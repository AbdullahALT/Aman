package com.amanapp.ui.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.DownloadFileTask;

import java.io.File;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public class DownloadOperation extends Operation<File> {

    private final String TAG = DownloadOperation.class.getName();

    public DownloadOperation(@NonNull Context context, @NonNull Activity activity, @NonNull FileSerialized file) {
        super(context, activity, file);
        action = FileAction.DOWNLOAD;
    }

    @Override
    protected void startAction(FileSerialized file) {
        new DownloadFileTask(
                context,
                DropboxClientFactory.getClient(),
                this
        ).execute(file);
        Log.v(TAG, "Start the Download File Task");
    }

    @Override
    public void onTaskComplete(File result) {
        dialog.dismiss();
        Log.v(TAG, "The file is downloaded");
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
        Log.v(TAG, "Failed Downloading the file");
    }
}
