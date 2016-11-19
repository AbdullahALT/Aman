package com.amanapp.server.Requests;

import com.amanapp.server.RetrofitClient;
import com.amanapp.server.exceptions.InvalidNumberOfParametersException;
import com.amanapp.server.exceptions.RequiredQueryException;

import java.util.Map;


/**
 * Created by Abdullah ALT on 11/18/2016.
 */
public class EmailAvailableRequest extends ServerRequest {

    @Override
    protected void checkQuery(Map<String, String> query) {
        if (query.size() != 1) {
            throw new InvalidNumberOfParametersException("sendRequest::EmailAvailableRequest accepts 1 parameter " +
                    " in the order [email]");
        }
        if (!query.containsKey("email")) {
            throw new RequiredQueryException();
        }
    }

    @Override
    void initRequest(Map<String, String> query) {
        AmanRequests requests = RetrofitClient.getClient().create(AmanRequests.class);
        call = requests.isEmailAvailable(query.get("email"));
    }
}
