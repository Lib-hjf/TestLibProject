package com.hjf.api;

import org.hjf.annotation.apt.ApiRepository;

import okhttp3.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * TODO Header 标记 BaseUrl
 */
@ApiRepository()
public interface ApiService {

    String namename = "212";

    @GET("login/{userName}/{userPWD}")
    Call login(@Path("userName") String userName, @Path("userPWD") String password);

    @GET("login/{userName}/{userPWD}")
    Call login2(@Path("userName") long userName, @Path("userPWD") int password, byte[] bytes);
}
