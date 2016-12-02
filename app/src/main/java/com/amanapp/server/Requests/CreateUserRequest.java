package com.amanapp.server.Requests;

import com.amanapp.server.RetrofitClient;
import com.amanapp.server.exceptions.InvalidNumberOfParametersException;
import com.amanapp.server.exceptions.RequiredQueryException;

import java.util.Map;

/**
 * Created by Abdullah ALT on 11/15/2016.
 */
class CreateUserRequest extends ServerRequest {

    @Override
    public void checkQuery(Map<String, String> query) {
        if (query.size() != 4) {
            throw new InvalidNumberOfParametersException("sendRequest::CreateUserRequest accepts 3 parameters " +
                    "in the order [email, password, salt]");
        } else if (!query.containsKey("email") || !query.containsKey("password")
                || !query.containsKey("salt") || !query.containsKey("authsecret")) {
            throw new RequiredQueryException();
        }
    }

    @Override
    public void initRequest(Map<String, String> query) {
        AmanRequests requests = RetrofitClient.getClient().create(AmanRequests.class);
        call = requests.createUser(query.get("email"), query.get("password"), query.get("salt"), query.get("authsecret"));
    }
}
