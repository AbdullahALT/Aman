package com.amanapp.application.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Abdullah ALT on 12/25/2016.
 */

public class PermissionsManager {

    private Context context;
    private Permission permissions;

    public PermissionsManager(Context context, Permission permission) {
        this.context = context;
        this.permissions = permission;
    }

    //TODO: This function checks only one permission requested, change to adapt for more
    public void onRequestPermissionsResult(String[] permissions, int[] grantResults, onPermissionResult permissionResult) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionResult.onPermissionDenied(permissions[i]);
                return;
            }
        }
        permissionResult.onPermissionGranted(permissions);
    }

    public boolean hasPermission() {
        for (String name : permissions.getPermissions()) {
            int result = ContextCompat.checkSelfPermission(context, name);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public boolean shouldDisplayRationaleForAction() {
        for (String name : permissions.getPermissions()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, name)) {
                return true;
            }
        }
        return false;
    }

    public void requestPermissionsForAction() {
        ActivityCompat.requestPermissions(
                (Activity) context,
                permissions.getPermissions(),
                permissions.getCode()
        );
    }

    public enum Permission {
        DOWNLOAD(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        UPLOAD(Manifest.permission.READ_EXTERNAL_STORAGE);

        private static final Permission[] values = values();

        private final String[] permissions;

        Permission(String... permissions) {
            this.permissions = permissions;
        }

        public static Permission fromCode(int code) {
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

    public interface onPermissionResult {
        void onPermissionDenied(String permission);

        void onPermissionGranted(String[] permissions);
    }
}
