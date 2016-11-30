package com.amanapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amanapp.R;
import com.amanapp.authentication.TwoFactorAuthUtil;

import java.security.GeneralSecurityException;

public class Authentication extends AppCompatActivity {

    protected EditText authenticationCode;
    protected Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        authenticationCode = (EditText) findViewById(R.id.authentication_code);
        submit = (Button) findViewById(R.id.submit_code);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TwoFactorAuthUtil auth = new TwoFactorAuthUtil();
                    String submittedCode = authenticationCode.getText().toString();
                    String code = auth.generateCurrentNumber("NY4A5CPJZ46LXZCP");

                    if (code.equals(submittedCode)) {
                        startActivity(new Intent(Authentication.this, ListFolderActivity.class));
                    } else {
                        //TODO: Set Action if the code doesn't match
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
