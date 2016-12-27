package com.amanapp.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amanapp.R;
import com.amanapp.authentication.TwoFactorAuthUtil;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;
import com.amanapp.server.ServerTask;
import com.amanapp.server.validation.Validation;

public class RegisterActivity extends LoginActivity implements ServerTask.Callback {

    protected EditText confirmationView;

    protected String confirmation;

    protected String authsecret;

    private Validation passwordValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        switchActivity();
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
        authsecret = new TwoFactorAuthUtil().generateBase32Secret();
        passwordValidation = Validation.Factory(Validation.ValidationType.REGISTRATION_PASSWORD);
        Log.d(TAG, "confirmation= [" + confirmation + "], authentication secret= [" + authsecret + "]");
    }

    @Override
    protected void setServerTask() {
        serverTask = new ServerTask(RegisterActivity.this, ServerRequest.RequestType.CREATE_USER, RegisterActivity.this) {
         //TODO Encrypt authentication

            @Override
            protected void addQueries(ServerConnect connect) {
                Log.d(TAG, "addQueries()");
                connect.addQuery("email", email).addQuery("password", password);
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
            if (!passwordValidation.validate(password)) {
                passwordView.setError(passwordValidation.getErrorMessages().get(0));
                focusView = passwordView;
                Log.d(TAG, "invalid password");
                return false;
            } else {
                if (!confirmation.equals(password)) {
                    confirmationView.setError(getString(R.string.error_confirm_password));
                    focusView = confirmationView;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void toNextActivity() {
        finish();
        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        intent.putExtra(WelcomeActivity.EXTRA_EMAIL, email);
        intent.putExtra(WelcomeActivity.EXTRA_SECRET, authsecret);
        startActivity(intent);
    }

}
