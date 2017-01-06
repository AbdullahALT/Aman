package com.amanapp.application.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.application.core.util.UriHelpers;
import com.amanapp.dropbox.Callback;
import com.amanapp.dropbox.PermissionOperation;
import com.amanapp.dropbox.UploadOperation;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "EXTRA_PATH";

    private static final String TAG = UploadActivity.class.getName();
    private static final int PICK_FILE_REQUEST_CODE = 1;

    private String path;
    private String uri;
    private PermissionOperation upload;

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
                if (!(uploadName.getText().toString().length() <= 0)) {
                    uploadFile();
                } else {
                    Toast.makeText(UploadActivity.this, "Enter the name of the file", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                File file = UriHelpers.getFileForUri(this, Uri.parse(uri));
                if (file != null) {
                    uploadName.setText(file.getName().split("\\.")[0]);
                }
            } else {
                finish();
            }
        }
    }

    private void uploadFile() {
        String name = uploadName.getText().toString();
        upload = new UploadOperation(this, path, name, uri, "Uploading...", new Callback<FileMetadata>() {
            @Override
            public void onTaskComplete(FileMetadata result) {
                Toast.makeText(UploadActivity.this, "The file has been uploaded", Toast.LENGTH_LONG).show();
                UploadActivity.this.finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(UploadActivity.this, "Error uploading the file", Toast.LENGTH_LONG).show();
                UploadActivity.this.finish();
            }
        });
        upload.performWithPermissions();
    }

    public void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        Log.v(TAG, "start Pick File Activity");
        startActivityForResult(Intent.createChooser(intent, "Choose File Picker"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        upload.onRequestPermissionsResult(permissions, grantResults);
    }
}
