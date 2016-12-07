package com.amanapp.application.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.authentication.TwoFactorAuthUtil;
import com.amanapp.logics.CurrentUser;
import com.amanapp.server.AmanResponse;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;

import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authentication extends AppCompatActivity implements Callback<AmanResponse> {

    protected EditText authenticationCode;
    protected Button submit;
    protected ProgressDialog dialog;
    protected ServerConnect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        authenticationCode = (EditText) findViewById(R.id.authentication_code);
        submit = (Button) findViewById(R.id.submit_code);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    //TODO: Build an abstract class to hold the repeated codes ( Important )
    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    protected void addQueries() {
        connect.addQuery("email", CurrentUser.get());
    }

    private void sendRequest() {
        connect.request(ServerRequest.RequestType.GET_AUTHSECRET, this);
    }

    private void request() {
        connect = new ServerConnect();
        addQueries();

        showDialog();

        sendRequest();
    }

    @Override
    public void onResponse(Call<AmanResponse> call, Response<AmanResponse> response) {
        if (response.isSuccessful()) {
            AmanResponse result = response.body();

            if (result.getSuccess() == 1) {

                try {
                    String submittedCode = authenticationCode.getText().toString();
                    String secret = result.getMessage();
                    String currentCode = new TwoFactorAuthUtil().generateCurrentNumber(secret);

                    if (currentCode.equals(submittedCode)) {
                        startActivity(new Intent(Authentication.this, ListFolderActivity.class));
                    } else {
                        Toast.makeText(AmanApplication.getContext(), "Authentication secret is incorrect", Toast.LENGTH_LONG).show();
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AmanApplication.getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {

            Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    @Override
    public void onFailure(Call<AmanResponse> call, Throwable t) {
        Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
        t.printStackTrace();
        dialog.dismiss();
    }
}
