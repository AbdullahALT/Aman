package com.amanapp.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.authentication.TwoFactorAuthUtil;
import com.amanapp.crypto.SecretKey;
import com.amanapp.logics.CurrentUser;
import com.amanapp.server.AmanResponse;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;
import com.amanapp.server.ServerTask;
import com.amanapp.server.validators.Validation;

import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Abdullah ALT on 11/19/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ServerTask.Callback {

    public static final String TAG = LoginActivity.class.getName();

    protected EditText passwordView;
    protected AutoCompleteTextView emailView;

    protected String email;
    protected String password;
    protected String authsecret;


    protected Button firstButton;
    protected Button secondButton;

    protected View focusView;
    protected ServerTask serverTask;
    private Validation emailValidation;
    private Validation passwordValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_form_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        setActionBarTitle();
        initViews();
        initListeners();
    }

    @Override
    public void onBackPressed() {
        //Override the onBackPressed and make it nothing to prevent user from going back
    }

    protected void setActionBarTitle() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("Login");
    }

    protected void initViews() {
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);

        firstButton = (Button) findViewById(R.id.first_button);
        secondButton = (Button) findViewById(R.id.second_button);

        firstButton.setText(R.string.action_sign_in_short);
        secondButton.setText(R.string.action_register);
    }

    protected void initListeners() {
        firstButton.setOnClickListener(this);
        secondButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_button:
                Log.d(TAG, "first button clicked");
                initServerRequest();
                break;
            case R.id.second_button:
                Log.d(TAG, "second button clicked");
                switchActivity();
                break;
        }
    }

    private void initServerRequest() {
        Log.d(TAG, "initServerRequest()");
        resetErrors();

        setValues();

        focusView = null;

        emailValidation = Validation.Factory(email, Validation.ValidationType.EMAIL);
        passwordValidation = Validation.Factory(password, Validation.ValidationType.PASSWORD);

        validate();

        if (focusView == null) {
            serverTask.request(dialogMessage());
        } else {
            focusView.requestFocus();
        }
    }


    protected void resetErrors() {
        Log.d(TAG, "resetErrors()");
        emailView.setError(null);
        passwordView.setError(null);
    }

    protected void setValues() {
        Log.d(TAG, "setValues()");
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        authsecret = new TwoFactorAuthUtil().generateBase32Secret();
        Log.d(TAG, "email= [" + email + "], password= [" + password + "]");
        setServerTask();
    }

    protected void setServerTask() {
        serverTask = new ServerTask(this, ServerRequest.RequestType.LOG_IN, this) {
            @Override
            protected void addQueries(ServerConnect connect) {
                Log.d(TAG, "addQueries()");
                connect.addQuery("email", email).addQuery("password", password);
                Log.d(TAG, "email= [" + email + "], password= [" + password + "]");
            }
        };
    }

    protected boolean validate() {
        Log.d(TAG, "validate()");
        if (!emailValidation.isValid()) {
            emailView.setError(emailValidation.getErrorMessages().get(0));
            focusView = emailView;
            Log.d(TAG, "invalid email");
            return false;
        } else if (!passwordValidation.isValid()) {
            passwordView.setError(passwordValidation.getErrorMessages().get(0));
            focusView = passwordView;
            Log.d(TAG, "invalid password");
            return false;
        }
        return true;
    }

    @NonNull
    protected String dialogMessage() {
        return getString(R.string.dialog_logging_in);
    }

    protected void switchActivity() {
        finish();
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    protected void toNextActivity() {
        finish();
        startActivity(new Intent(LoginActivity.this, Authentication.class));
    }

    @Override
    public void onTaskSuccess(Call<AmanResponse> call, Response<AmanResponse> response) {

        try {
            CurrentUser.set(email);
            SecretKey.init(password, authsecret);
            toNextActivity();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTaskFailure(Call<AmanResponse> call, Response<AmanResponse> response) {
        Toast.makeText(AmanApplication.getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskError(Call<AmanResponse> call, Response<AmanResponse> response) {
        Log.d(TAG, "unsuccessful response");
        Log.d(TAG, "code: " + response.code());
        Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onException(Call<AmanResponse> call, Throwable t) {
        Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Network error");

        t.printStackTrace();
    }


//    @Override
//    public void onResponse(Call<AmanResponse> call, Response<AmanResponse> response) {
//        if (response.isSuccessful()) {
//            AmanResponse result = response.body();
//
//            if (result.getSuccess() == 1) {
//                CurrentUser.set(email);
//                toNextActivity();
//            } else {
//                Toast.makeText(AmanApplication.getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            Log.d(TAG, "successful response");
//            Log.d(TAG, response.body().getMessage());
//
//        } else {
//            Log.d(TAG, "unsuccessful response");
//            Log.d(TAG, "code: " + response.code());
//
//            Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
//        }
//        dialog.dismiss();
//    }
//
//    @Override
//    public void onFailure(Call<AmanResponse> call, Throwable t) {
//        Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
//        Log.d(TAG, "Network error");
//
//        t.printStackTrace();
//        dialog.dismiss();
//    }
}
