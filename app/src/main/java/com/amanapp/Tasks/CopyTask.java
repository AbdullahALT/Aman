package com.amanapp.Tasks;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;


/**
 * Created by Abdullah ALT on 7/23/2016.
 */
public class CopyTask extends Task<String, Void, Metadata>{

    public CopyTask(DbxClientV2 dropboxClient, Callback<Metadata> callback) {
        super(dropboxClient, callback);
    }

    @Override
    protected Metadata doInBackground(String... params) {
        try{
            return dropboxClient.files().copy(params[0], params[1]);
        } catch ( DbxException e){
            exception = e;
        }

        return null;
    }

}
