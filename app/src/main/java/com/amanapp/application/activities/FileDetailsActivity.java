package com.amanapp.application.activities;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.dropbox.Callback;
import com.amanapp.dropbox.DeleteTask;
import com.amanapp.dropbox.DownloadOperation;
import com.amanapp.dropbox.DropboxClient;
import com.amanapp.dropbox.Operation;
import com.amanapp.logics.FileSerialized;
import com.dropbox.core.v2.files.Metadata;

public class FileDetailsActivity extends AppCompatActivity {

    public final static String SERIALIZED_FILE = "SERIALIZED_FILE";
    private final static String TAG = FileDetailsActivity.class.getName();

    private FileSerialized file;
    private Operation download;

    private ImageView fileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        Intent intent = getIntent();

        file = ((FileSerialized) intent.getSerializableExtra(SERIALIZED_FILE));

        fileImage = (ImageView) findViewById(R.id.file_image);
        file.getThumbnail().into(fileImage);

        TextView name = (TextView) findViewById(R.id.file_name);
        name.setText(file.getName());

        TextView type = (TextView) findViewById(R.id.file_type);
        type.setText(file.getExtension());

        ImageView icon = (ImageView) findViewById(R.id.file_icon);
        icon.setImageResource(file.getIcon());

        TextView size = (TextView) findViewById(R.id.file_size);
        size.setText(file.getSize());

        TextView folder = (TextView) findViewById(R.id.file_folder);
        folder.setText(file.getFolder());

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
                download = new DownloadOperation(FileDetailsActivity.this, FileDetailsActivity.this, file);
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
                                        new DeleteTask(DropboxClient.getClient(),
                                                new Callback<Metadata>() {
                                                    @Override
                                                    public void onTaskComplete(Metadata result) {
                                                        Log.v(TAG, "The file " + result.getName() + " has been deleted");
                                                        Toast.makeText(AmanApplication.getContext(),
                                                                "The file has been deleted",
                                                                Toast.LENGTH_LONG).show();
                                                        finish();

                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.v(TAG, "Error deleting the file: " + e.getMessage());
                                                        Toast.makeText(AmanApplication.getContext(),
                                                                "An error has occurred",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                        ).execute(file.getPathLower());
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
        download.onRequestPermissionsResult(permissions, grantResults, "Downloading");
    }

}
