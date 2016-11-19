package com.amanapp.server.Requests;

import com.amanapp.server.RetrofitClient;
import com.amanapp.server.exceptions.InvalidNumberOfParametersException;
import com.amanapp.server.exceptions.RequiredQueryException;

import java.util.Map;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
class LoginRequest extends ServerRequest {

    @Override
    protected void checkQuery(Map<String, String> query) {
        if (query.size() != 2) {
            throw new InvalidNumberOfParametersException("sendRequest::CreateUserRequest accepts 2 parameters " +
                    " in the order [email, password]");
        } else if (!query.containsKey("email") || !query.containsKey("password")) {
            throw new RequiredQueryException();
        }
    }

    @Override
    void initRequest(Map<String, String> query) {
        AmanRequests requests = RetrofitClient.getClient().create(AmanRequests.class);
        call = requests.login(query.get("email"), query.get("password"));
    }
}
