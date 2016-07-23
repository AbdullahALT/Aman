package com.amanapp.UserInterface.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.amanapp.Connections.PicassoClient;
import com.amanapp.Connections.DropboxClientFactory;

import com.dropbox.core.android.Auth;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends AppCompatActivity {
    /*
     * SharedPreferences is a way to store persistent data using pair keys. what this method do is:
     * OnResume, get the shared preferences then get the stored access token from it, if there is
     * no access token start the authentication process, and if the access token's stored
     * initiate the application
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                prefs.edit().putString("access-token", accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        PicassoClient.init(getApplicationContext(), DropboxClientFactory.getClient());
        loadData();
    }

    protected abstract void loadData();

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }
}
