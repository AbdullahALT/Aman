package com.amanapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.amanapp.R;
import com.amanapp.server.Requests.ServerRequest;

public class RegisterActivity extends LoginActivity {

    protected EditText confirmationView;

    protected String confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    //TODO: Random Salt Operation!
    @Override
    protected void addQueries() {
        super.addQueries();
        connect.addQuery("salt", "defaultSalt");
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


    //    @Override
//    protected void initServerRequest() {
//        emailView.setError(null);
//        passwordView.setError(null);
//        confirmationView.setError(null);
//
//        String email = emailView.getText().toString();
//        String password = passwordView.getText().toString();
//        String confirmation = confirmationView.getText().toString();
//
//        View focusView = null;
//
//        Validation emailValidation = Validation.Factory(email, Validation.ValidationType.EMAIL);
//        Validation passwordValidation = Validation.Factory(password, Validation.ValidationType.PASSWORD);
//
//        if(!emailValidation.isValid()){
//            emailView.setError(emailValidation.getErrorMessages().get(0));
//            focusView = emailView;
//        } else if (!passwordValidation.isValid()){
//            passwordView.setError(passwordValidation.getErrorMessages().get(0));
//            focusView = passwordView;
//        } else if(!password.equals(confirmation)){
//            confirmationView.setError(getString(R.string.error_confirm_password));
//            focusView = confirmationView;
//        }
//
//        if(focusView != null){
//            focusView.requestFocus();
//        } else {
//            connect();
//        }
//    }
}
