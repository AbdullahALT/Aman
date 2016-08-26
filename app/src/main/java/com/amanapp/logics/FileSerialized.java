package com.amanapp.logics;

import android.content.Context;
import android.content.res.Resources;

import com.amanapp.AmanApplication;
import com.amanapp.R;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Abdullah ALT on 8/18/2016.
 */
public class FileSerialized implements Serializable {
    private String name;
    private long size;
    private String pathDisplay;
    private String pathLower;
    private Date created;
    private Date modified;
    private String extension;
    private String folder;
    private String rev;

    public FileSerialized(FileMetadata file) {
        size = file.getSize();
        pathDisplay = file.getPathDisplay();
        pathLower = file.getPathLower();
        created = file.getClientModified();
        modified = file.getServerModified();
        rev = file.getRev();
        setter();
    }

    //TODO: Move this function to a better class
    private void setter() {
        String parts[] = pathDisplay.split("\\.");
        extension = parts[parts.length - 1].toLowerCase();
        parts = parts[0].split("/");
        int index = parts.length - 2;
        name = parts[index + 1];
        folder = ((index < 2) ? "Home" : parts[index]);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return name +"."+ extension;
    }

    public String getSize() {
        int unit = 1024;
        if (size < unit) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format(Locale.US, "%.1f %sB", size / Math.pow(unit, exp), pre);
    }

    public String getPathDisplay() {
        return pathDisplay;
    }

    public String getPathLower() {
        return pathLower;
    }

    public String getCreated() {
        return getString(created);
    }

    public String getModified() {
        return getString(modified);
    }

    public String getExtension() {
        return extension;
    }

    public String getFolder() {
        return folder;
    }

    public String getRev() {
        return rev;
    }

    private String getString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    //TODO: Move this function to a better class
    public int getIcon() {
        Context context = AmanApplication.getContext();
        Resources resources = context.getResources();
        int icon = resources.getIdentifier("icon_" + extension, "drawable", context.getPackageName());
        return (icon != 0) ? icon : R.drawable.ic_insert_drive_file_black_24dp;
    }
}
