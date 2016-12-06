package com.amanapp.application.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.authentication.TwoFactorAuthUtil;
import com.amanapp.logics.CurrentUser;
import com.squareup.picasso.Picasso;

/**
 * Created by Abdullah ALT on 12/1/2016.
 */

public class QrCode2 extends AppCompatActivity {

    public final static String EXTRA_SECRET = "EXTRA_SECRET";
    public final static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private TextView emailTextView;
    private ImageView qrCodeImage;
    private Button proceedButton;
    private Button sendButton;
    private CardView instructionCard;
    private TextView instructionDetails;
    private String secretKey;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code2);

        Intent intent = getIntent();
        secretKey = intent.getExtras().getString(EXTRA_SECRET);
        email = intent.getExtras().getString(EXTRA_EMAIL);

        emailTextView = (TextView) findViewById(R.id.email_text);

        qrCodeImage = (ImageView) findViewById(R.id.qr_code);

        proceedButton = (Button) findViewById(R.id.proceed_button);
        sendButton = (Button) findViewById(R.id.send_button);

        instructionCard = (CardView) findViewById(R.id.instruction_card);
        instructionDetails = (TextView) findViewById(R.id.instruction_details);

        TwoFactorAuthUtil auth = new TwoFactorAuthUtil();
        Picasso.with(this)
                .load(auth.qrImageUrl(getResources().getString(R.string.app_name), secretKey))
                .placeholder(R.drawable.ic_photo_grey_600_36dp)
                .into(qrCodeImage);

        emailTextView.setText(email);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QrCode2.this, ListFolderActivity.class));
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToAuthenticator();
            }
        });

        instructionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (instructionDetails.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(instructionCard);
                    instructionDetails.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(instructionCard);
                    instructionDetails.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendToAuthenticator() {
        String packageName = "com.google.android.apps.authenticator2";
        PackageManager manager = AmanApplication.getContext().getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            Intent authIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("otpauth://totp/Aman%20App:" + CurrentUser.get() + "?secret=" + secretKey + "&issuer=Aman%20App"));
            startActivity(authIntent);
        } else {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https:/play.google.com/store/apps//details?id=" + packageName)));
            }
        }
    }
}
