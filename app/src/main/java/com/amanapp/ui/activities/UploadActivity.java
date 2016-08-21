package com.amanapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amanapp.R;
import com.amanapp.ui.models.Operation;
import com.amanapp.ui.models.UploadOperation;
import com.amanapp.utilities.UriHelpers;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "EXTRA_PATH";

    private static final String TAG = UploadActivity.class.getName();
    private static final int PICK_FILE_REQUEST_CODE = 1;

    private String path;
    private String uri;

    private EditText uploadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Log.v(TAG, "onCreate Started");
        path = getIntent().getStringExtra(EXTRA_PATH);
        Log.v(TAG, "path is: " + path);
        Log.v(TAG, "Launch File Picker");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Activity");

        uploadName = (EditText) findViewById(R.id.upload_name);
        Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        if(uri == null ){
            launchFilePicker();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult");
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            Log.v(TAG, "requestCode is PICK_FILE_REQUEST_CODE");
            if (resultCode == RESULT_OK) {
                // This is the result of a call to launchFilePicker
                Log.v(TAG, "resultCode is RESULT_OK: " + data.getData().getPath());
                uri = data.getData().toString();
                //uploadFile(data.getData().toString());
            }
        }
    }

    private void uploadFile() {
        String name = uploadName.getText().toString();
        Operation upload = new UploadOperation(this, this, path, name , uri);
        upload.performWithPermissions(Operation.FileAction.UPLOAD, "Uploading");
    }

    public void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        Log.v(TAG, "start Pick File Activity");
        startActivityForResult(Intent.createChooser(intent, "Choose File Picker"), PICK_FILE_REQUEST_CODE);
    }
}
