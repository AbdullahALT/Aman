package com.amanapp.server;

import com.amanapp.server.Requests.ServerRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;

/**
 * Created by Abdullah ALT on 11/16/2016.
 */
//TODO: Change ServerConnect class name?
public class ServerConnect {
    ServerRequest request;
    Map<String, String> queries;

    public ServerConnect() {
        queries = new HashMap<>();
    }

    /**
     * Add queries to the request
     *
     * @param query the name of the query
     * @param value the value of the query
     * @return always return this object to allow nested calls for making a clean code
     */
    public ServerConnect addQuery(String query, String value) {
        queries.put(query, value);
        return this;
    }

    public void request(ServerRequest.RequestType requestType, Callback<AmanResponse> callback) {
        request = ServerRequest.Factory(requestType);
        request.request(queries, callback);
    }
}
