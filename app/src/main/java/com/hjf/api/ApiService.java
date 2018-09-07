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

    @GET("login/{userName}/{userPWD}")
    Call login(@Path("userName") String userName, @Path("userPWD") String password);

}
