package com.amanapp.application.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.amanapp.dropbox.Callback;

/**
 * Created by Abdullah ALT on 8/19/2016.
 */
public abstract class PermissionOperation<Return> implements Callback<Return>, PermissionsManager.onPermissionResult {

    //TODO: Handle the onComplete and onError function in a better way
    //TODO: The waiting process for the operation might be in a separate class to easily change them later
    protected ProgressDialog dialog;
    protected Context context;
    private PermissionsManager permissionsManager;
    private String waitingMessage;
    private Callback<Return> callback;

    public PermissionOperation(Context context, PermissionsManager.Permission action, String waitingMessage, Callback<Return> callback) {
        this.context = context;
        this.permissionsManager = new PermissionsManager(context, action);
        this.waitingMessage = waitingMessage;
        this.callback = callback;
    }

    protected abstract void startAction();

    public void performWithPermissions() {
        if (hasPermissionsForAction()) {
            performAction();
            return;
        }

        if (shouldDisplayRationaleForAction()) {
            new AlertDialog.Builder(context)
                    .setMessage("This app requires storage access to download and upload files.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissionsForAction();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissionsForAction();
        }
    }

    public void onRequestPermissionsResult(@NonNull final String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(permissions, grantResults, this);
    }

    protected void performAction() {
        onWaiting();
        startAction();
    }

    //Abstracted to be overridden by the concrete classes if they don't have any waiting process
    protected void onWaiting() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(waitingMessage);
        dialog.show();
    }

    @Override
    public void onTaskComplete(Return result) {
        dialog.dismiss();
        if (callback != null) {
            callback.onTaskComplete(result);
        }
    }

    @Override
    public void onError(Exception e) {
        dialog.dismiss();
        if (callback != null) {
            callback.onError(e);
        }
    }

    protected boolean hasPermissionsForAction() {
        return permissionsManager.hasPermission();
    }

    protected boolean shouldDisplayRationaleForAction() {
        return permissionsManager.shouldDisplayRationaleForAction();
    }

    protected void requestPermissionsForAction() {
        permissionsManager.requestPermissionsForAction();
    }


}
