package com.amanapp.server.validators;

import com.amanapp.server.AmanResponse;
import com.amanapp.server.Requests.ServerRequest;
import com.amanapp.server.ServerConnect;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abdullah ALT on 11/13/2016.
 */
class EmailAvailableValidator implements Validator {

    /**
     * Connects to the server to check if the passed email is an available email or not. this
     * function dosn't in any way send a message to the user to tell him about any network error,
     * but it returns true. beware that there is another side of this validation in the server side
     * so even if this function returned true because of network error, it can't pass that
     * validation
     *
     * @param message the email to be validate
     * @return false only if the email is unavailable, true otherwise
     */
    @Override
    public boolean validate(String message) {
        ServerConnect connect = new ServerConnect();
        connect.addQuery("email", message);
        final boolean[] result = {false};
        connect.request(ServerRequest.RequestType.EMAIL_AVAILABLE, new Callback<AmanResponse>() {
            @Override
            public void onResponse(Call<AmanResponse> call, Response<AmanResponse> response) {
                //true if the response not successful or true if the body's success
                result[0] = !response.isSuccessful() || (response.body().getSuccess() == 1);
            }

            @Override
            public void onFailure(Call<AmanResponse> call, Throwable t) {
                //true if a network exception accursed
                result[0] = true;
            }
        });

        return result[0];
    }

    @Override
    public String getErrorMessage() {
        return "Email has been already taken";
    }


}
