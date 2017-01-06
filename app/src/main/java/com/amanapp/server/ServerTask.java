package com.amanapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.amanapp.application.activities.LoginActivity;
import com.amanapp.server.Requests.ServerRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abdullah ALT on 12/7/2016.
 */

public abstract class ServerTask implements Callback<AmanResponse> {

    private Context context;
    private ServerConnect connect;
    private ProgressDialog dialog;
    private ServerRequest.RequestType requestType;
    private Callback callback;

    public ServerTask(Context context, ServerRequest.RequestType requestType, Callback callback) {
        this.context = context;
        this.requestType = requestType;
        this.callback = callback;
    }

    public final void request(String waitingMessage) {
        connect = new ServerConnect();
        addQueries(connect);

        showDialog(waitingMessage);

        sendRequest();
    }

    protected abstract void addQueries(ServerConnect connect);

    private void showDialog(String waitingMessage) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(waitingMessage);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void sendRequest() {
        Log.d(LoginActivity.TAG, "sendRequest()");
        connect.request(requestType, this);
    }

    @Override
    public void onResponse(Call<AmanResponse> call, Response<AmanResponse> response) {
        if (response.isSuccessful()) {
            AmanResponse result = response.body();

            if (result.getSuccess() == 1) {
                callback.onTaskSuccess(call, response);
            } else {
                callback.onTaskFailure(call, response);
            }

        } else {

            callback.onTaskError(call, response);
        }
        dialog.dismiss();
    }

    @Override
    public void onFailure(Call<AmanResponse> call, Throwable t) {
        callback.onException(call, t);
        dialog.dismiss();
    }

    public interface Callback {
        void onTaskSuccess(Call<AmanResponse> call, Response<AmanResponse> response);

        void onTaskFailure(Call<AmanResponse> call, Response<AmanResponse> response);

        void onTaskError(Call<AmanResponse> call, Response<AmanResponse> response);

        void onException(Call<AmanResponse> call, Throwable t);
    }
}
