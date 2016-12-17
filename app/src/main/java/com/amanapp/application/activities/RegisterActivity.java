package com.amanapp.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amanapp.R;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;
import com.amanapp.server.ServerTask;

public class RegisterActivity extends LoginActivity implements ServerTask.Callback {

    protected EditText confirmationView;

    protected String confirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBarTitle() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("Register");
    }

    @Override
    protected void initViews() {
        super.initViews();
        confirmationView = (EditText) findViewById(R.id.passwordConformation);
        confirmationView.setVisibility(View.VISIBLE);

        firstButton.setText(R.string.action_register_short);
        secondButton.setText(R.string.action_sign_in);
    }

    @Override
    protected void switchActivity() {
        finish();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    @Override
    protected void resetErrors() {
        super.resetErrors();
        confirmationView.setError(null);
    }

    @Override
    protected void setValues() {
        super.setValues();
        confirmation = confirmationView.getText().toString();
        Log.d(TAG, "confirmation= [" + confirmation + "], authentication secret= [" + authsecret + "]");
    }

    @Override
    protected void setServerTask() {
        serverTask = new ServerTask(RegisterActivity.this, ServerRequest.RequestType.CREATE_USER, RegisterActivity.this) {
            @Override
            protected void addQueries(ServerConnect connect) {
                Log.d(TAG, "addQueries()");
                connect.addQuery("email", email).addQuery("password", password);
                connect.addQuery("salt", "defaultSalt");
                connect.addQuery("authsecret", authsecret);
                Log.d(TAG, "email= [" + email + "], password= [" + password + "], authsecret=[ " + authsecret + "]");
            }
        };
    }

    @NonNull
    @Override
    protected String dialogMessage() {
        return getString(R.string.dialog_registering);
    }


    @Override
    protected boolean validate() {
        if (super.validate()) {
            if (!confirmation.equals(password)) {
                confirmationView.setError(getString(R.string.error_confirm_password));
                focusView = confirmationView;
                return false;
            }
        }
        return true;
    }

    @Override
    protected void toNextActivity() {
        finish();
        Intent intent = new Intent(RegisterActivity.this, QrCode.class);
        intent.putExtra(QrCode.EXTRA_EMAIL, email);
        intent.putExtra(QrCode.EXTRA_SECRET, authsecret);
        startActivity(intent);
    }

}
