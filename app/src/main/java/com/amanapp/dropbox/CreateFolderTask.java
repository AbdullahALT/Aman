package com.amanapp.dropbox;

import android.support.annotation.NonNull;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;

/**
 * Created by Abdullah ALT on 12/20/2016.
 */

public class CreateFolderTask extends Task<String, Metadata> {

    public CreateFolderTask(@NonNull DbxClientV2 dropboxClient, @NonNull Callback<Metadata> callback) {
        super(dropboxClient, callback);
    }

    @Override
    protected Metadata doInBackground(String... params) {
        try {
            String path = params[0] + '/' + params[1];
            return dropboxClient.files().createFolder(path);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }
}
