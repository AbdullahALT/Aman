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
import com.amanapp.application.activities.elements.NameAlert;
import com.amanapp.application.core.PermissionOperation;
import com.amanapp.application.core.logics.FileSerialized;
import com.amanapp.dropbox.Callback;
import com.amanapp.dropbox.DeleteTask;
import com.amanapp.dropbox.DownloadOperation;
import com.amanapp.dropbox.DropboxClient;
import com.amanapp.dropbox.MoveTask;
import com.dropbox.core.v2.files.Metadata;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public class FileDetailsActivity extends AppCompatActivity {

    public final static String SERIALIZED_FILE = "SERIALIZED_FILE";
    private final static String TAG = FileDetailsActivity.class.getName();

    private FileSerialized file;
    private PermissionOperation download;

    private ImageView fileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        Intent intent = getIntent();

        file = ((FileSerialized) intent.getSerializableExtra(SERIALIZED_FILE));

        fileImage = (ImageView) findViewById(R.id.file_image);
        RequestCreator requestCreator = file.getThumbnail();
        if (requestCreator != null) requestCreator.into(fileImage);

        final TextView fileName = (TextView) findViewById(R.id.file_name);
        fileName.setText(file.getName());

        ImageView changeName = (ImageView) findViewById(R.id.change_name_icon);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameAlert nameAlert = new NameAlert(FileDetailsActivity.this, "Change", "Change", new NameAlert.onPositive() {
                    @Override
                    public void click(final String name) {
                        if (name.length() > 0) {
                            new MoveTask(DropboxClient.getClient(), new Callback<Metadata>() {
                                @Override
                                public void onTaskComplete(Metadata result) {
                                    fileName.setText(name);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(AmanApplication.getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }).execute(file.getPathLower(), file.getParentPath().toLowerCase() + name + "." + file.getExtension());
                        } else {
                            Toast.makeText(AmanApplication.getContext(), "No name found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                nameAlert.show();
            }
        });

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton downloadButton = (FloatingActionButton) findViewById(R.id.fab);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                download = new DownloadOperation(FileDetailsActivity.this, file, getString(R.string.downloading), new Callback<File>() {
                    @Override
                    public void onTaskComplete(File result) {
                        Toast.makeText(AmanApplication.getContext(), "The file has been downloaded", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AmanApplication.getContext(), "Error downloading the file", Toast.LENGTH_LONG).show();
                    }
                });
                download.performWithPermissions();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(FileDetailsActivity.this)
                        .setTitle(file.getName() + "." + file.getExtension())
                        .setMessage(R.string.delete_file_alert_message)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.v(TAG, "Start Deleting");
                                        new DeleteTask(DropboxClient.getClient(),
                                                new Callback<Metadata>() {
                                                    @Override
                                                    public void onTaskComplete(Metadata result) {
                                                        Log.v(TAG, "The file " + result.getName() + " has been deleted");
                                                        Toast.makeText(AmanApplication.getContext(),
                                                                R.string.toast_delete_success,
                                                                Toast.LENGTH_LONG).show();
                                                        finish();

                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.v(TAG, "Error deleting the file: " + e.getMessage());
                                                        Toast.makeText(AmanApplication.getContext(),
                                                                R.string.error_occurred,
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
        download.onRequestPermissionsResult(permissions, grantResults);
    }

}
