package com.amanapp.application.core.logics;

/**
 * Created by Abdullah ALT on 11/21/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class manages the use of Access Token, if the user has registered before then most likely the access token is still valid,
 * so this class save so the user won't have to go through the authorization process again
 */
public class AccessManager {
    private SharedPreferences preferencesFile;

    public AccessManager(Context context) {
        preferencesFile = context.getSharedPreferences("AccessTokens", 0);
    }

    public String getUserAccessToken(String email) {
        return preferencesFile.getString(email, null);
    }

    public void setUserAccessToken(String email, String accessToken) {
        preferencesFile.edit().putString(email, accessToken).apply();
    }

    public boolean hasToken(String email) {
        return preferencesFile.contains(email);
    }

}
