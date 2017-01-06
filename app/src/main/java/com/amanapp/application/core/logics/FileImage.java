package com.amanapp.application.core.logics;

import android.content.Context;
import android.content.res.Resources;
import android.webkit.MimeTypeMap;

import com.amanapp.R;
import com.amanapp.application.core.util.FileThumbnailRequestHandler;
import com.squareup.picasso.RequestCreator;

import java.io.Serializable;

/**
 * Created by Abdullah ALT on 8/29/2016.
 */
public class FileImage implements Serializable {

    protected String extension;
    protected String pathLower;

    public FileImage(String extension, String pathLower) {
        this.extension = extension;
        this.pathLower = pathLower;
    }

    int getIcon() {
        Context context = AmanApplication.getContext();
        Resources resources = context.getResources();
        int icon = resources.getIdentifier("icon_" + extension, "drawable", context.getPackageName());
        if (icon == 0) {
            return isImage() ? R.drawable.ic_photo_grey_600_36dp : R.drawable.ic_insert_drive_file_black_24dp;
        }
        return icon;
    }

    RequestCreator getThumbnail() {
        if (isImage()) {
            return PicassoClient.getPicasso().load(FileThumbnailRequestHandler.buildPicassoUri(pathLower));
        } else {
            return null;
        }
    }

    String getMimeType() {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(extension);
    }

    boolean isImage() {
        String type = getMimeType();
        return type != null && type.startsWith("image/");
    }
}
