package com.amanapp.server.Requests;

import com.amanapp.server.AmanResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Abdullah ALT on 11/14/2016.
 */

interface AmanRequests {
    @FormUrlEncoded
    @POST("create_user.php")
    Call<AmanResponse> createUser(@Field("email") String email, @Field("password") String password, @Field("salt") String salt, @Field("authsecret") String authSecret);

    @FormUrlEncoded
    @POST("log_in.php")
    Call<AmanResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("get_salt.php")
    Call<AmanResponse> getSalt(@Field("email") String email);

    @FormUrlEncoded
    @POST("email_availability.php")
    Call<AmanResponse> isEmailAvailable(String email);

    @FormUrlEncoded
    @POST("get_authsecret.php")
    Call<AmanResponse> getAuthSecret(@Field("email") String email);
}
