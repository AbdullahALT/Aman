package com.amanapp.tasks.callbacks;

import android.app.Activity;
import android.util.Log;

import com.dropbox.core.v2.files.Metadata;

/**
 * Created by Abdullah ALT on 8/20/2016.
 */
public class DeleteCallback implements Callback<Metadata> {

    private final static String TAG = DeleteCallback.class.getName();
    private Activity activity;

    public DeleteCallback(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onTaskComplete(Metadata result) {
        Log.v(TAG, "Task Complete");
        activity.finish();
    }

    @Override
    public void onError(Exception e) {
        Log.v(TAG, "Error Task " + e.getMessage());
    }
}
