package com.amanapp.application.core.logics;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Abdullah ALT on 8/27/2016.
 */
public class FilePath implements Serializable {

    protected String path;
    protected String fullName;
    protected String[] pathParts;
    protected String[] nameParts;

    FilePath(@NonNull String path) {
        this.path = path;
        this.pathParts = path.split("/");
        this.fullName = pathParts[pathParts.length - 1];
        this.nameParts = fullName.split("\\.");
    }

    //TODO: handle the case if the extension is the extension of encrypted files
    String getExtension() {
        return nameParts[nameParts.length - 1].toLowerCase();
    }

    String getFullExtensions() {
        String extensions = "";
        for (int i = 1; i < nameParts.length; i++) {
            extensions = extensions.concat(".");
            extensions = extensions.concat(nameParts[i]);
        }
        return extensions;
    }

    String getFolder() {
        int folderIndex = pathParts.length - 2;
        return isHomeFolder(folderIndex) ? "Home" : pathParts[folderIndex];
    }

    String getPathDisplay() {
        return path;
    }

    String getName() {
        return nameParts[0];
    }

    String getFullName() {
        return fullName;
    }


    String getParentPath() {
        String parent = "";
        for (int i = 0; i < (pathParts.length - 1); i++) {
            parent += pathParts[i] + "/";
        }
        return parent;
    }
    private boolean isHomeFolder(int folderIndex) {
        return folderIndex < 2;
    }
}
