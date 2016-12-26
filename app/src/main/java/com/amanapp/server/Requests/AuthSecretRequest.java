package com.amanapp.server.Requests;

import com.amanapp.application.core.exceptions.InvalidNumberOfParametersException;
import com.amanapp.application.core.exceptions.RequiredQueryException;
import com.amanapp.server.RetrofitClient;

import java.util.Map;

/**
 * Created by Abdullah ALT on 12/6/2016.
 */
public class AuthSecretRequest extends ServerRequest {

    @Override
    protected void checkQuery(Map<String, String> query) {
        if (query.size() != 1) {
            throw new InvalidNumberOfParametersException("sendRequest::AuthSecretRequest accepts 1 parameter " +
                    " in the order [email]");
        } else if (!query.containsKey("email")) {
            throw new RequiredQueryException();
        }
    }

    @Override
    void initRequest(Map<String, String> query) {
        AmanRequests requests = RetrofitClient.getClient().create(AmanRequests.class);
        call = requests.getAuthSecret(query.get("email"));
    }
}
