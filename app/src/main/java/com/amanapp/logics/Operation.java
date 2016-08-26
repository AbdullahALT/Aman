package com.amanapp.logics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.amanapp.tasks.callbacks.Callback;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public abstract class Operation<Return> implements Callback<Return> {

    //TODO: Handle the onComplete and onError function in a better way
    //TODO: The waiting process for the operation might be in a separate class to easily change them later
    protected ProgressDialog dialog;
    protected Context context;
    protected Activity activity;
    protected FileAction action;

    public Operation(Context context, Activity activity, FileAction action) {
        this.context = context;
        this.activity = activity;
        this.action = action;

    }

    protected abstract void startAction();

    protected abstract void onPermissionDenied(String permission);

    protected abstract void onPermissionGranted(String waitingMessage);

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

    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults, String waitingMessage) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                onPermissionDenied(permissions[i]);
                return;
            }
        }
        onPermissionGranted(waitingMessage);
    }

    protected void performAction(FileAction action, String waitingMessage) {
        if (this.action == action) {
            onWaiting(waitingMessage);
            startAction();
        }
    }

    protected void onWaiting(String waitingMessage) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(waitingMessage);
        dialog.show();
    }

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
