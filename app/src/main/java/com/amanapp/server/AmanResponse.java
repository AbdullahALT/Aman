package com.amanapp.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */

public class AmanResponse {
    @SerializedName("success")
    protected int success;

    @SerializedName("message")
    protected String message;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
