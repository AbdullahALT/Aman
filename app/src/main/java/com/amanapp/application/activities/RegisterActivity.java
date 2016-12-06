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

public class RegisterActivity extends LoginActivity {

    protected EditText confirmationView;

    protected String confirmation;

    private String authsecret;

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
        requestType = ServerRequest.RequestType.CREATE_USER;
        authsecret = new TwoFactorAuthUtil().generateBase32Secret();
        Log.d(TAG, "confirmation= [" + confirmation + "], request type= [" + requestType + "], authentication secret= [" + authsecret + "]");
    }

    //TODO: Random Salt Operation!
    @Override
    protected void addQueries() {
        super.addQueries();
        connect.addQuery("salt", "defaultSalt");
        connect.addQuery("authsecret", authsecret);
        Log.d(TAG, "salt= [defaultSalt], authentication secret =[" + authsecret + "]");
    }

    @NonNull
    @Override
    protected CharSequence dialogMessage() {
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
        Intent intent = new Intent(RegisterActivity.this, QrCode2.class);
        intent.putExtra(QrCode2.EXTRA_EMAIL, email);
        intent.putExtra(QrCode2.EXTRA_SECRET, authsecret);
        startActivity(intent);
    }

}
