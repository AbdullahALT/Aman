package com.amanapp.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amanapp.R;
import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.tasks.DeleteTask;
import com.amanapp.tasks.callbacks.DeleteCallback;
import com.amanapp.ui.models.DownloadOperation;
import com.amanapp.ui.models.FileSerialized;
import com.amanapp.ui.models.Operation;

public class FileDetailsActivity extends AppCompatActivity {

    public final static String SERIALIZED_FILE = "SERIALIZED_FILE";
    private final static String TAG = FileDetailsActivity.class.getName();
    private FileSerialized file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        Intent intent = getIntent();

        file = ((FileSerialized) intent.getSerializableExtra(SERIALIZED_FILE));

        TextView name = (TextView) findViewById(R.id.file_name);
        name.setText(file.getName());

        TextView type = (TextView) findViewById(R.id.file_type);
        type.setText(file.getExtension());

        TextView size = (TextView) findViewById(R.id.file_size);
        size.setText(file.getSize());

        TextView location = (TextView) findViewById(R.id.file_location);
        location.setText(file.getFolder());

        TextView created = (TextView) findViewById(R.id.file_created);
        created.setText(file.getCreated());

        TextView modified = (TextView) findViewById(R.id.file_modified);
        modified.setText(file.getModified());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton downloadButton = (FloatingActionButton) findViewById(R.id.fab);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Operation download = new DownloadOperation(FileDetailsActivity.this, FileDetailsActivity.this, file);
                download.performWithPermissions(Operation.FileAction.DOWNLOAD, "Downloading");
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(FileDetailsActivity.this)
                        .setMessage("Do you want to delete " + file.getPathDisplay())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.v(TAG, "Start Deleting");
                                        new DeleteTask(DropboxClientFactory.getClient(), new DeleteCallback(FileDetailsActivity.this)).execute(file.getPathLower());
                                    }
                                }
                        )
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
