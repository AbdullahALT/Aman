package com.amanapp.cnnections;

import android.content.Context;
import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Singleton instance of Picasso pre-configured
 */
public class PicassoClient {
    private static final String TAG = PicassoClient.class.getName();
    private static Picasso sPicasso;

    public static void init(Context context, DbxClientV2 dbxClient) {

        // Configure picasso to know about special thumbnail requests
        sPicasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context))
                .addRequestHandler(new FileThumbnailRequestHandler(dbxClient))
                .build();

        Log.v(TAG, "picasso is null: " + (sPicasso == null));
    }


    public static Picasso getPicasso() {
        return sPicasso;
    }
}
