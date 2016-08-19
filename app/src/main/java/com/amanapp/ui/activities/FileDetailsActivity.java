package com.amanapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amanapp.R;
import com.amanapp.ui.models.FileSerialized;

public class FileDetailsActivity extends AppCompatActivity {

    private FileSerialized file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        Intent intent = getIntent();

        file = ((FileSerialized) intent.getSerializableExtra("Serialized_File"));

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
            public void onClick(View view) {
                Snackbar.make(view, "Action for downloading the file", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
