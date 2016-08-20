package com.amanapp.ui.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.UploadFileTask;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class UploadOperation extends Operation<FileMetadata> {

    public UploadOperation(@NonNull Context context, @NonNull Activity activity, @NonNull FileSerialized file) {
        super(context, activity, file);
    }

    @Override
    protected void startAction(FileSerialized file) {
        new UploadFileTask(context, DropboxClientFactory.getClient(), this);
    }

    @Override
    public void onTaskComplete(FileMetadata result) {
        dialog.dismiss();
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
    }
}
