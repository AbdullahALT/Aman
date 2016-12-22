package com.amanapp.application.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amanapp.R;
import com.amanapp.application.AmanApplication;
import com.amanapp.application.elements.DividerItemDecoration;
import com.amanapp.application.elements.MetadataAdapter;
import com.amanapp.application.elements.NameAlert;
import com.amanapp.cnnections.PicassoClient;
import com.amanapp.dropbox.Callback;
import com.amanapp.dropbox.CreateFolderTask;
import com.amanapp.dropbox.DropboxClient;
import com.amanapp.dropbox.ListFolderTask;
import com.amanapp.logics.FileSerialized;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

public class ListFolderActivity extends DropboxActivity implements MetadataAdapter.onMetaDataClick, View.OnClickListener {

    public final static String EXTRA_PATH = "DESIRED_PATH";
    private final static String TAG = ListFolderActivity.class.getName();
    private String currentPath;
    private MetadataAdapter metadataAdapter;

    private RecyclerView filesView;
    private Toolbar toolbar;
    private FloatingActionButton addButton;
    private TextView noDropboxAccountText;

    public static Intent getIntent(Context context, String path) {
        Intent intent = new Intent(context, ListFolderActivity.class);
        intent.putExtra(ListFolderActivity.EXTRA_PATH, path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_folder);
        Log.v(TAG, "Entered onCreate");

        String path = getIntent().getStringExtra(EXTRA_PATH);
        currentPath = (path == null) ? "" : path;

        Log.v(TAG, "The path is " + currentPath);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Files List");
        //TODO: Set Logo For The Toolbar

        noDropboxAccountText = (TextView) findViewById(R.id.no_dropbox_message);

        addButton = (FloatingActionButton) findViewById(R.id.addButton);

        filesView = (RecyclerView) findViewById(R.id.filesView);
        filesView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, R.drawable.divider);
        filesView.addItemDecoration(decoration);


        Log.v(TAG, "OnCreate finished");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String accessToken = Auth.getOAuth2Token();
        if (accessToken != null) {
            saveAccessToken(accessToken);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected boolean onNotSavedAccessToken() {

        noDropboxAccountText.setVisibility(View.VISIBLE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.startOAuth2Authentication(ListFolderActivity.this, getString(R.string.app_key));

                Log.v(TAG, "UploadButton has been clicked");
            }
        });
        return false;
    }

    @Override
    protected boolean onSavedAccessToken() {
        if (!currentPath.equals("")) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        noDropboxAccountText.setVisibility(View.INVISIBLE);
        getSupportActionBar().setSubtitle((currentPath.equals("") ? "Home" : "Home" + currentPath));

        addButton.setOnClickListener(this);
        return false;
    }

    @Override
    protected void loadData() {

        Log.v(TAG, "Entered loadData");

        Log.v(TAG, "Set the list adapter");
        metadataAdapter = new MetadataAdapter(PicassoClient.getPicasso(), this);
        filesView.setAdapter(metadataAdapter);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading");
        dialog.show();

        Log.v(TAG, "ListFolderTask started");
        new ListFolderTask(DropboxClient.getClient(),
                new Callback<ListFolderResult>() {
                    @Override
                    public void onTaskComplete(ListFolderResult result) {
                        Log.v(TAG, "List Folder Task Completed");

                        dialog.dismiss();
                        metadataAdapter.setItems(result.getEntries());
                    }

                    @Override
                    public void onError(Exception e) {
                        dialog.dismiss();

                        Toast.makeText(AmanApplication.getContext(),
                                "An error has occurred",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        ).execute(currentPath);
    }

    @Override
    public void onFileClicked(FileMetadata file) {
        Log.v(TAG, "the file " + file.getName() + " has been clicked");
        Intent intent = new Intent(this, FileDetailsActivity.class);
        intent.putExtra(FileDetailsActivity.SERIALIZED_FILE, new FileSerialized(file));
        startActivity(intent);

    }

    @Override
    public void onFolderClicked(FolderMetadata folder) {
        Log.v(TAG, "the folder " + folder.getName() + " has been clicked");
        startActivity(ListFolderActivity.getIntent(ListFolderActivity.this, folder.getPathLower()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        if (hasToken()) {
            menu.findItem(R.id.delete_dropbox).setVisible(true);
        } else {
            menu.findItem(R.id.delete_dropbox).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: Change all of Main Menu's items
        if (item.getItemId() == R.id.sign_out) {
            SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
            prefs.edit().remove("access-token").apply();
            currentPath = null;
            finish();
            startActivity(new Intent(ListFolderActivity.this, LoginActivity.class));
        } else if (item.getItemId() == R.id.feedback) {
            /* Create the Intent */
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"aman@abdullahalt.me"});

            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } else if (item.getItemId() == R.id.delete_dropbox) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Dropbox")
                    .setMessage("Your Aman account will lose control to your Dropbox, but don't worry your encrypted files cna still be decrypted if you add this Dropbox again.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveAccessToken(null);
                            startActivity(ListFolderActivity.getIntent(ListFolderActivity.this, ""));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!currentPath.equals("")) {
            super.onBackPressed();
        } else {
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.addCategory(Intent.CATEGORY_HOME);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backHome);
        }
    }

    //The onClick listener for the add button
    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(this)
                .setItems(R.array.add_dialog_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            uploadFile();
                        } else if (i == 1) {
                            createFolder();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    private void uploadFile() {
        Log.v(TAG, "add button has been clicked");
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra(UploadActivity.EXTRA_PATH, currentPath);
        startActivity(intent);
    }

    private void createFolder() {

        NameAlert nameAlert = new NameAlert(this, "Create Folder", "Create", new NameAlert.onPositive() {
            @Override
            public void click(String name) {
                new CreateFolderTask(DropboxClient.getClient(), new Callback<Metadata>() {
                    @Override
                    public void onTaskComplete(Metadata result) {
                        ListFolderActivity.this.finish();
                        startActivity(ListFolderActivity.getIntent(ListFolderActivity.this, currentPath));
                        Toast.makeText(ListFolderActivity.this, "Folder has been created", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AmanApplication.getContext(), "Error creating folder", Toast.LENGTH_LONG).show();
                    }
                }).execute(currentPath, name);
            }
        });

        nameAlert.show();
    }
}
