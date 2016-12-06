package com.amanapp.application.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.authentication.TwoFactorAuthUtil;
import com.amanapp.logics.CurrentUser;
import com.squareup.picasso.Picasso;

public class QrCode extends AppCompatActivity {

    protected ImageView qrCode;
    protected Button syncButton;
    protected Button scannedButton;

    protected String privateKey;
    protected String name;
    protected String secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initViews();
        initImage();
        initListeners();

    }

    protected void initViews() {
        qrCode = (ImageView) findViewById(R.id.qr_code);
        syncButton = (Button) findViewById(R.id.sync_button);
        scannedButton = (Button) findViewById(R.id.scanned_button);

    }

    protected void initImage() {
        TwoFactorAuthUtil auth = new TwoFactorAuthUtil();
        //TODO: Change secret to make it random (Critical)
//        secret = auth.generateBase32Secret();
        secret = "NY4A5CPJZ46LXZCP";
        name = "Aman App";
        privateKey = auth.qrImageUrl(name, secret);

//        PicassoClient.getPicasso().load(privateKey)
//                .placeholder(R.drawable.ic_photo_grey_600_36dp)
//                .error(R.drawable.ic_photo_grey_600_36dp)
//                .into(qrCode);
        Picasso.with(this).load(privateKey).into(qrCode);

    }

    protected void initListeners() {
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = "com.google.android.apps.authenticator2";
                PackageManager manager = AmanApplication.getContext().getPackageManager();
                Intent intent = manager.getLaunchIntentForPackage(packageName);
                if (intent == null) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https:/play.google.com/store/apps//details?id=" + packageName)));
                    }
                    return;
                }
                Intent authIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("otpauth://totp/Aman%20App:" + CurrentUser.get() + "?secret=" + secret + "&issuer=Aman%20App"));
                startActivity(authIntent);
            }
        });

        scannedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QrCode.this, ListFolderActivity.class));
            }
        });
    }


}
