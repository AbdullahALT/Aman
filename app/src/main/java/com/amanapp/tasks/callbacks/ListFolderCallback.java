package com.amanapp.tasks.callbacks;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amanapp.ui.models.MetadataAdapter;
import com.dropbox.core.v2.files.ListFolderResult;

/**
 * Created by Abdullah ALT on 8/12/2016.
 */
public class ListFolderCallback implements Callback<ListFolderResult> {

    private final static String TAG = ListFolderCallback.class.getName();

    private final Context context;
    private final ProgressDialog dialog;
    private final MetadataAdapter metadataAdapter;

    public ListFolderCallback(Context context, ProgressDialog dialog, MetadataAdapter metadataAdapter) {
        this.context = context;
        this.dialog = dialog;
        this.metadataAdapter = metadataAdapter;
    }

    @Override
    public void onTaskComplete(ListFolderResult result) {
        Log.v(TAG, "Task Completed");
        dialog.dismiss();
        metadataAdapter.setItems(result.getEntries());
    }

    @Override
    public void onError(Exception e) {
        Log.v(TAG, "Error in the task");
        Log.v(TAG, e.getMessage());
        dialog.dismiss();

        Toast.makeText(context,
                "An error has occurred",
                Toast.LENGTH_SHORT)
                .show();
    }
}
