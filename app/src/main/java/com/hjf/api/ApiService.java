package com.hjf.api;

import com.hjf.model.UserInfo;

import org.hjf.annotation.apt.ApiRepository;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * TODO Header 标记 BaseUrl
 */
@ApiRepository()
public interface ApiService {

    @GET("login/{userName}/{userPWD}")
    Call login(@Path("userName") String userName, @Path("userPWD") String password);

    @GET("login/{userName}/{userPWD}")
    okhttp3.Call login2(@Path("userName") long userName, @Path("userPWD") int password, byte[] bytes);

    @POST("dadd")
    Call<UserInfo> playMusic(@Body HashMap<String, String> param);
}
