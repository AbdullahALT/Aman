package com.amanapp.Tasks;

/**
 * Created by Abdullah ALT on 7/22/2016.
 */
public interface Callback<Return> {

    void onTaskComplete(Return result);
    void onError(Exception e);

}