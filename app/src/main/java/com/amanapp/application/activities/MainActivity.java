package com.amanapp.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amanapp.logics.CurrentUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: handle the way this main activity interact if the user choose to be automatically logged in.
        //in this way, current user will always be null
        if (CurrentUser.get() == null) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            finish();
            startActivity(new Intent(MainActivity.this, ListFolderActivity.class));
        }
    }
}
