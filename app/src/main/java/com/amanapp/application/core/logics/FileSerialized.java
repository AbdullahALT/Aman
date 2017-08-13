package com.amanapp.application.core.logics;

import android.support.annotation.Nullable;

import com.dropbox.core.v2.files.FileMetadata;
import com.squareup.picasso.RequestCreator;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Abdullah ALT on 8/18/2016.
 */
public class FileSerialized implements Serializable {
    protected long size;
    protected String pathLower;
    protected String rev;
    protected FileDates dates;
    protected FilePath path;
    protected FileImage image;

    public FileSerialized(FileMetadata file) {
        size = file.getSize();
        pathLower = file.getPathLower();
        rev = file.getRev();
        dates = new FileDates(file.getClientModified(), file.getServerModified());
        path = new FilePath(file.getPathDisplay());
        image = new FileImage(getExtension(), pathLower);
    }

    public String getName() {
        return path.getName();
    }

    public String getFullName() {
        return path.getFullName();
    }

    public String getSize() {
        int unit = 1024;
        if (size < unit) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format(Locale.US, "%.1f %sB", size / Math.pow(unit, exp), pre);
    }

    public String getPathDisplay() {
        return path.getPathDisplay();
    }

    public String getPathLower() {
        return pathLower;
    }

    public String getCreated() {
        return dates.getCreated();
    }

    public String getModified() {
        return dates.getModified();
    }

    public String getExtension() {
        return path.getExtension();
    }

    public String getFullExtension() {
        return path.getFullExtensions();
    }

    public String getFolder() {
        return path.getFolder();
    }

    public String getRev() {
        return rev;
    }

    public String getParentPath() {
        return path.getParentPath();
    }

    //TODO: Move this function to a better class
    public int getIcon() {
        return image.getIcon();
    }

    @Nullable
    public RequestCreator getThumbnail() {
        return image.getThumbnail();
    }
}
