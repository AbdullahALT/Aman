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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanapp.R;
import com.amanapp.cnnections.DropboxClientFactory;
import com.amanapp.cnnections.PicassoClient;
import com.amanapp.logics.DownloadOperation;
import com.amanapp.logics.FileSerialized;
import com.amanapp.logics.Operation;
import com.amanapp.tasks.DeleteTask;
import com.amanapp.tasks.FileThumbnailRequestHandler;
import com.amanapp.tasks.callbacks.DeleteCallback;
import com.squareup.picasso.Picasso;

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
        setFileImage(PicassoClient.getPicasso());

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
        download.onRequestPermissionsResult(permissions, grantResults, "Downloading");
    }

    private void setFileImage(Picasso picasso) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = file.getExtension();
        String type = mime.getMimeTypeFromExtension(extension);
        if (type != null && type.startsWith("image/")) {
            Log.v(TAG, "item file is an image (Name:" + file.getName() + ")");
            picasso.load(FileThumbnailRequestHandler.buildPicassoUri(file))
                    .placeholder(R.drawable.ic_photo_grey_600_36dp)
                    .error(R.drawable.ic_photo_grey_600_36dp)
                    .into(fileImage);
        } else {
            Log.v(TAG, "item file is not an image (Name:" + file.getName() + ")");
            picasso.load(R.drawable.ic_insert_drive_file_blue_36dp)
                    .noFade()
                    .into(fileImage);
        }
    }
}
