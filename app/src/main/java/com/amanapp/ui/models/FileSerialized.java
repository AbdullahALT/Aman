package com.amanapp.ui.models;

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

    public FileSerialized(FileMetadata file) {
        size = file.getSize();
        pathDisplay = file.getPathDisplay();
        pathLower = file.getPathLower();
        created = file.getClientModified();
        modified = file.getServerModified();
        setter();
    }

    private void setter() {
        String parts[] = pathDisplay.split("\\.");
        extension = parts[1];
        parts = parts[0].split("/");
        int index = parts.length - 2;
        name = parts[index + 1];
        folder = ((index < 2) ? "Home" : parts[index]);
    }

    public String getName() {
        return name;
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

    private String getString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        return dateFormat.format(date);
    }
}
