package com.amanapp.tasks;

import android.util.Log;

import com.amanapp.tasks.callbacks.Callback;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;

/**
 * Async task to list items in a folder
 */
public class ListFolderTask extends Task<String, Void, ListFolderResult> {

    private final static String TAG = ListFolderTask.class.getName();

    public ListFolderTask(DbxClientV2 dropboxClient, Callback<ListFolderResult> callback) {
        super(dropboxClient, callback);
        Log.v(TAG, "DropboxClient: " + (dropboxClient == null));
    }

    @Override
    protected ListFolderResult doInBackground(String... params) {
        try {
            Log.v(TAG, "doInBackground");
            Log.v(TAG, "params[0]: " + params[0]);
            return dropboxClient.files().listFolder(params[0]);
        } catch (DbxException e) {
            exception = e;
        }

        return null;
    }
}
