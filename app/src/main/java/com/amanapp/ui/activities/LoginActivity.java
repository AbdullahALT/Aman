package com.amanapp.ui.activities;

import android.app.ProgressDialog;
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

import com.amanapp.AmanApplication;
import com.amanapp.R;
import com.amanapp.logics.CurrentUser;
import com.amanapp.server.AmanResponse;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;
import com.amanapp.server.validators.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abdullah ALT on 11/19/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<AmanResponse> {

    public static final String TAG = LoginActivity.class.getName();

    protected EditText passwordView;
    protected AutoCompleteTextView emailView;

    protected String email;
    protected String password;

    protected Button firstButton;
    protected Button secondButton;

    protected ProgressDialog dialog;
    protected ServerConnect connect;

    protected ServerRequest.RequestType requestType;
    protected View focusView;

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
        resetErrors();

        setValues();

        focusView = null;

        emailValidation = Validation.Factory(email, Validation.ValidationType.EMAIL);
        passwordValidation = Validation.Factory(password, Validation.ValidationType.PASSWORD);

        validate();

        if (focusView == null) {
            request();
        } else {
            focusView.requestFocus();
        }
    }

    private void request() {
        connect = new ServerConnect();
        addQueries();

        showDialog();

        sendRequest();
    }

    protected void resetErrors() {
        emailView.setError(null);
        passwordView.setError(null);
    }

    protected void setValues() {
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        requestType = ServerRequest.RequestType.LOG_IN;
        Log.d(TAG, "email= [" + email + "], password= [" + password + "], request type= [" + requestType + "]");
    }

    protected boolean validate() {
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

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(dialogMessage());
        dialog.show();
    }

    @NonNull
    protected CharSequence dialogMessage() {
        return getString(R.string.dialog_logging_in);
    }

    protected void addQueries() {
        connect.addQuery("email", email).addQuery("password", password);
    }

    protected void switchActivity() {
        finish();
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    protected void toNextActivity() {
        finish();
        startActivity(new Intent(LoginActivity.this, Authentication.class));
    }

    private void sendRequest() {
        connect.request(requestType, this);
    }

    @Override
    public void onResponse(Call<AmanResponse> call, Response<AmanResponse> response) {
        if (response.isSuccessful()) {
            AmanResponse result = response.body();

            if (result.getSuccess() == 1) {
                CurrentUser.set(email);
                toNextActivity();
            } else {
                Toast.makeText(AmanApplication.getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
            }

            Log.v(TAG, "successful response");
            Log.v(TAG, response.body().getMessage());

        } else {
            Log.v(TAG, "unsuccessful response");
            Log.v(TAG, "code: " + response.code());

            Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    @Override
    public void onFailure(Call<AmanResponse> call, Throwable t) {
        Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
        Log.v(TAG, "Network error");

        t.printStackTrace();
        dialog.dismiss();
    }
}
