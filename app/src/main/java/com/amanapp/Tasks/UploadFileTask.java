package com.amanapp.Tasks;

import android.content.Context;
import android.net.Uri;

import com.amanapp.Utlities.UriHelpers;
import com.dropbox.core.DbxException;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTask extends Task<String, Void, FileMetadata> {

    private final Context context;


    public UploadFileTask(Context context, DbxClientV2 dropboxClient, Callback<FileMetadata> callback) {
        super(dropboxClient, callback);
        this.context = context;
    }

    @Override
    protected FileMetadata doInBackground(String... params) {
        String localUri = params[0];
        File localFile = UriHelpers.getFileForUri(context, Uri.parse(localUri));

        if (localFile != null) {
            String remoteFolderPath = params[1];

            // Note - this is not ensuring the name is a valid dropbox file name
            String remoteFileName = localFile.getName();

            try (InputStream inputStream = new FileInputStream(localFile)) {
                return dropboxClient.files().uploadBuilder(remoteFolderPath + "/" + remoteFileName)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(inputStream);
            } catch (DbxException | IOException e) {
                exception = e;
            }
        }

        return null;
    }
}
