package com.amanapp.ui.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.UploadFileTask;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class UploadOperation extends Operation<FileMetadata> {

    private final String uri;
    private final String path;
    private final String name;

    public UploadOperation(@NonNull Context context, @NonNull Activity activity, @NonNull String path, @NonNull String name, @NonNull String uri) {
        super(context, activity, FileAction.UPLOAD);
        this.path = path;
        this.name = name;
        this.uri = uri;
    }

    @Override
    protected void startAction() {
        new UploadFileTask(context, DropboxClientFactory.getClient(), this).execute(path, name, uri);
    }

    @Override
    public void onTaskComplete(FileMetadata result) {
        dialog.dismiss();
        Toast.makeText(context, "The file has been uploaded", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
        Toast.makeText(context, "Error uploading the file", Toast.LENGTH_LONG).show();
    }
}
