package com.hjf.api;

import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjf.ApiRepository;
import com.hjf.MyApp;

import org.hjf.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 进行初始化操作：
 * 1. 选定 http 工具
 * 2. 存放 Retrofit.build() 对象，进行全局配置
 * -    除开 baseUrl() 的设置，因为不用的 ApiService 可能不同的地址
 */
public class ApiConfigUtil {

    public static final int HOST_Query_Host = 1;
    public static final int HOST_Connect = 2;
    public static final int HOST_Business = 3;

    private static final int TIME_OUT_CONNECT = 7676;
    private static final int TIME_OUT_READ = 7676;
    private static final String CACHE_DIR_PATH = "com.hjf";// TODO
    private static final int CACHE_SIZE = 1024 * 1024 * 100;

    private static final SparseArray<String> hostCacheArray = new SparseArray<>();
    private static Retrofit retrofit;

    /**
     * init api repository
     *
     * step 1. get host array for {@link HostInterceptor}
     * -    switch retrofit base url at runtime.
     * step 2. get retrofit object for create All Api Service with annotation {@link org.hjf.annotation.apt.ApiRepository}
     */
    public static void init() {
        hostCacheArray.put(HOST_Query_Host, "http://www.a371369.cn/");
        ApiRepository.init(retrofit = getRetrofit());
        ApiRepository.ApiServiceImpl.login(new HashMap<String, String>()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private static Retrofit getRetrofit() {
        // 1. config ok http
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT_READ, TimeUnit.MILLISECONDS)
                // head
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })
                // http log
                .addInterceptor(new HttpLoggingInterceptor())
                // host interceptor : retrofit base url switch
                .addNetworkInterceptor(new HostInterceptor())
                // http cache
                .addNetworkInterceptor(new HttpCacheInterceptor())
                // cache 100Mb
                .cache(new Cache(new File(CACHE_DIR_PATH, "cache"), CACHE_SIZE))
                .build();

        // 2. config google json
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        // 3. config retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                // TODO custom converter
                .addConverterFactory(GsonConverterFactory.create(gson))
                // Not to set base url, use HostInterceptor switch base url at runtime.
                // .baseUrl("host")
                .build();

        return retrofit;
    }

    // TODO 缓存检查
    private static final class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isNetConnected(MyApp.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.d("OkHttp", "no network");
            }

            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isNetConnected(MyApp.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    // 监听请求，根据需要切换 BaseUrl TODO 判断Header参数
    private static final class HostInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            ApiConfigUtil.hostCacheArray.get(0);
            return null;
        }
    }
}
