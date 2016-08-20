package com.amanapp.ui.models;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.amanapp.tasks.callbacks.Callback;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public abstract class Operation<Return> implements Callback<Return> {

    //TODO: Handle the onComplete and onError function in a better way
    protected final ProgressDialog dialog;
    protected Context context;
    protected Activity activity;
    protected FileSerialized file;
    protected FileAction action;

    public Operation(Context context, Activity activity, FileSerialized file) {
        this.context = context;
        this.activity = activity;
        this.file = file;
        this.dialog = new ProgressDialog(context);
    }

    public void performWithPermissions(final FileAction action, String waitingMessage) {
        if (hasPermissionsForAction(action)) {
            performAction(action, waitingMessage);
            return;
        }

        if (shouldDisplayRationaleForAction(action)) {
            new AlertDialog.Builder(context)
                    .setMessage("This app requires storage access to download and upload files.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissionsForAction(action);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissionsForAction(action);
        }
    }

    protected void performAction(FileAction action, String waitingMessage) {
        if (this.action == action) {
            onWaiting(waitingMessage);
            startAction(file);
        }
    }

    protected void onWaiting(String waitingMessage) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(waitingMessage);
        dialog.show();
    }

    protected abstract void startAction(FileSerialized file);

    protected boolean hasPermissionsForAction(FileAction action) {
        for (String permission : action.getPermissions()) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    protected boolean shouldDisplayRationaleForAction(FileAction action) {
        for (String permission : action.getPermissions()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    protected void requestPermissionsForAction(FileAction action) {
        ActivityCompat.requestPermissions(
                activity,
                action.getPermissions(),
                action.getCode()
        );
    }

    public enum FileAction {
        DOWNLOAD(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        UPLOAD(Manifest.permission.READ_EXTERNAL_STORAGE);

        private static final FileAction[] values = values();

        private final String[] permissions;

        FileAction(String... permissions) {
            this.permissions = permissions;
        }

        public static FileAction fromCode(int code) {
            if (code < 0 || code >= values.length) {
                throw new IllegalArgumentException("Invalid FileAction code: " + code);
            }
            return values[code];
        }

        public int getCode() {
            return ordinal();
        }

        public String[] getPermissions() {
            return permissions;
        }
    }
}
