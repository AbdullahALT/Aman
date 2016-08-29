package com.amanapp.dropbox;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by Abdullah ALT on 7/20/2016.
 */
public abstract class Task<Params, Process, Return> extends AsyncTask<Params, Process, Return> {

    protected final DbxClientV2 dropboxClient;
    private final Callback<Return> callback;
    protected Exception exception;
    private boolean isSuccessful;

    public Task(@NonNull DbxClientV2 dropboxClient, @NonNull Callback<Return> callback) {
        this.dropboxClient = dropboxClient;
        this.callback = callback;
        this.isSuccessful = false;
    }

    @Override
    protected abstract Return doInBackground(Params... params);

    @Override
    protected void onPostExecute(Return result) {
        super.onPostExecute(result);
        if (exception != null) {
            callback.onError(exception);
        } else if (result == null) {
            callback.onError(new NullPointerException("Result is null"));
        } else {
            callback.onTaskComplete(result);
            isSuccessful = true;
        }
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Exception getException() {
        return exception;
    }
}
