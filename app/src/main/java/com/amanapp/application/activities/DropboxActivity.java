package com.amanapp.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.amanapp.application.core.logics.AccessManager;
import com.amanapp.application.core.logics.CurrentUser;
import com.amanapp.application.core.logics.PicassoClient;
import com.amanapp.dropbox.DropboxClient;

/**
 * Created by Abdullah ALT on 11/21/2016.
 */
public abstract class DropboxActivity extends AppCompatActivity {
    private AccessManager accessManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessManager = new AccessManager(DropboxActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String accessToken = accessManager.getUserAccessToken(CurrentUser.get());
        if (accessToken == null) {
            onNotSavedAccessToken();
        } else {
            onSavedAccessToken();
            initAndLoadData(accessToken);
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClient.init(accessToken);
        PicassoClient.init(getApplicationContext(), DropboxClient.getClient());
        loadData();
    }

    protected void saveAccessToken(String accessToken) {
        accessManager.setUserAccessToken(CurrentUser.get(), accessToken);
    }

    protected boolean hasToken() {
        return accessManager.hasToken(CurrentUser.get());
    }

    protected abstract void loadData();

    /**
     * the set of actions to be done in case there is an access token
     */
    protected boolean onSavedAccessToken() {
        return false;
    }

    /**
     * the set of actions to be done in case there is no access token
     *
     * @return true if the access token is now saved after calling this method, false otherwise
     */
    protected boolean onNotSavedAccessToken() {
        return false;
    }
}
