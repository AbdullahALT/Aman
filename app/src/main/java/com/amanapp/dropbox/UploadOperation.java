package com.amanapp.dropbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.amanapp.application.core.PermissionOperation;
import com.amanapp.application.core.PermissionsManager;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class UploadOperation extends PermissionOperation<FileMetadata> {

    private final String uri;
    private final String path;
    private final String name;
    private Callback<FileMetadata> callback;

    public UploadOperation(@NonNull Context context, @NonNull String path, @NonNull String name, @NonNull String uri, String waitingMessage, Callback<FileMetadata> callback) {
        super(context, PermissionsManager.Permission.UPLOAD, waitingMessage, callback);
        this.path = path;
        this.name = name;
        this.uri = uri;
        this.callback = callback;
    }

    @Override
    protected void startAction() {
        new UploadFileTask(context, DropboxClient.getClient(), this).execute(path, name, uri);
    }

    @Override
    public void onPermissionDenied(String permission) {
        Toast.makeText(context,
                "Can't upload file: read access denied. " +
                        "Please grant storage permissions to use this functionality.",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        performAction();
    }
}
