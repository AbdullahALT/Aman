package com.amanapp.ui.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.cnnections.PicassoClient;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends AppCompatActivity {

    private final static String TAG = DropboxActivity.class.getName();

    /*
     * SharedPreferences is a way to store persistent data using pair keys. what this method do is:
     * OnResume, get the shared preferences then get the stored access token from it, if there is
     * no access token start the authentication process, and if the access token's stored
     * initiate the application
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "OnResume");

        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            Log.v(TAG, "accessToken is not saved in the shared preferences");
            onNotSavedAccessToken();
        } else {
            Log.v(TAG, "accessToken is saved in the shared preferences");
            onSavedAccessToken();
            initAndLoadData(accessToken);
        }
    }

    protected void saveAccessToken(String accessToken) {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        prefs.edit().putString("access-token", accessToken).apply();
    }

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

    private void initAndLoadData(String accessToken) {
        Log.v(TAG, "Entered initAndLoadData");
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
