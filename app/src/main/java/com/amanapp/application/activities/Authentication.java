package com.amanapp.application.activities;

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
import com.amanapp.server.ServerTask;

import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Response;

public class Authentication extends AppCompatActivity {

    protected EditText authenticationCode;
    protected Button submit;
    protected ServerTask serverTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        authenticationCode = (EditText) findViewById(R.id.authentication_code);
        submit = (Button) findViewById(R.id.submit_code);

        serverTask = new ServerTask(this, ServerRequest.RequestType.GET_AUTHSECRET, new ServerTask.Callback() {
            @Override
            public void onTaskSuccess(Call<AmanResponse> call, Response<AmanResponse> response) {
                try {
                    String submittedCode = authenticationCode.getText().toString();
                    String secret = response.body().getMessage();
                    String currentCode = new TwoFactorAuthUtil().generateCurrentNumber(secret);

                    if (currentCode.equals(submittedCode)) {
                        startActivity(new Intent(Authentication.this, ListFolderActivity.class));
                    } else {
                        Toast.makeText(AmanApplication.getContext(), "Authentication secret is incorrect", Toast.LENGTH_LONG).show();
                    }
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
                Toast.makeText(AmanApplication.getContext(), R.string.error_connect_to__server, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Call<AmanResponse> call, Throwable t) {
                t.printStackTrace();
            }
        }) {
            @Override
            protected void addQueries(ServerConnect connect) {
                connect.addQuery("email", CurrentUser.get());
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverTask.request(getString(R.string.dialog_authentication));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Authentication.this, LoginActivity.class));
    }
}
