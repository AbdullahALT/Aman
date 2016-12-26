package com.amanapp.server.Requests;

import com.amanapp.application.core.exceptions.ForcedConstantParameterException;
import com.amanapp.server.AmanResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
public abstract class ServerRequest {

    protected Call<AmanResponse> call;

    public static ServerRequest Factory(RequestType type) {
        switch (type) {
            case LOG_IN:
                return new LoginRequest();
            case CREATE_USER:
                return new CreateUserRequest();
            case EMAIL_AVAILABLE:
                return new EmailAvailableRequest();
            case GET_AUTHSECRET:
                return new AuthSecretRequest();
            default:
                throw new ForcedConstantParameterException("ServerRequest's Factory accepts only its public static constants or their " +
                        "corresponding values");
        }
    }

    /**
     * Classes inhering from this class can choose to override this method for checking the number of strings in the query
     *
     * @param query : an array of strings to be checked
     */
    protected void checkQuery(Map<String, String> query) {

    }

    abstract void initRequest(Map<String, String> query);

    protected void sendRequest(Callback<AmanResponse> callback) {
        call.enqueue(callback);
    }

    public void request(Map<String, String> query, Callback<AmanResponse> callback) {
        try {
            checkQuery(query);
            initRequest(query);
            sendRequest(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum RequestType {
        LOG_IN, CREATE_USER, EMAIL_AVAILABLE, GET_AUTHSECRET
    }

}
