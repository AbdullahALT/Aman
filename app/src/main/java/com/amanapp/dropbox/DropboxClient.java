package com.amanapp.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Singleton instance of {@link DbxClientV2}
 */
public class DropboxClient {

    private static DbxClientV2 sDbxClient;

    private DropboxClient() {
    }

    public static void init(String accessToken) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("Aman/0.0.1")
                    .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                    .build();

            sDbxClient = new DbxClientV2(requestConfig, accessToken);
    }

    public static DbxClientV2 getClient() {
        if (sDbxClient == null) {
            throw new IllegalStateException("Client not initialized.");
        }
        return sDbxClient;
    }
}