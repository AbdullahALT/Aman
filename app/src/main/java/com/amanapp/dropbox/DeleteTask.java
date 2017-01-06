package com.amanapp.dropbox;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class DeleteTask extends Task<String, Metadata> {

    private final static String TAG = DeleteTask.class.getName();

    public DeleteTask(@NonNull DbxClientV2 dropboxClient, @NonNull Callback<Metadata> callback) {
        super(dropboxClient, callback);
    }

    @Override
    protected Metadata doInBackground(String... params) {
        try {
            Log.v(TAG, "Deleting " + params[0]);
            return dropboxClient.files().delete(params[0]);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }
}
