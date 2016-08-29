package com.amanapp.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;

/**
 * Created by Abdullah ALT on 7/23/2016.
 */
public class MoveTask extends Task<String, Void, Metadata> {
    public MoveTask(DbxClientV2 dropboxClient, Callback<Metadata> callback) {
        super(dropboxClient, callback);
    }

    @Override
    protected Metadata doInBackground(String... params) {
        try {
            String fromPath = params[0];
            String toPath = params[1];
            return dropboxClient.files().move(fromPath, toPath);
        } catch (DbxException e) {
            exception = e;
        }

        return null;
    }
}
