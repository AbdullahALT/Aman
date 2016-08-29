package com.amanapp.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

/**
 * Async task for getting user account info
 */
public class GetCurrentAccountTask extends Task<Void, Void, FullAccount> {

    private FullAccount result;

    public GetCurrentAccountTask(DbxClientV2 dropboxClient, Callback<FullAccount> callback) {
        super(dropboxClient, callback);
    }

    @Override
    protected FullAccount doInBackground(Void... params) {

        try {
            return dropboxClient.users().getCurrentAccount();

        } catch (DbxException e) {
            exception = e;
        }

        return null;
    }

    public FullAccount getResult() {
        return result;
    }
}
