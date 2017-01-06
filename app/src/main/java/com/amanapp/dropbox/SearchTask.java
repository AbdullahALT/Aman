package com.amanapp.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.SearchResult;

/**
 * Created by Abdullah ALT on 7/23/2016.
 */
public class SearchTask extends Task<String, SearchResult> {

    public SearchTask(DbxClientV2 dropboxClient, Callback<SearchResult> callback) {

        super(dropboxClient, callback);
    }

    @Override
    protected SearchResult doInBackground(String... params) {
        try {
            String path = params[0];
            String query = params[1];
            return dropboxClient.files().search(path, query);
        } catch (DbxException e) {
            exception = e;
        }
        return null;
    }
}
