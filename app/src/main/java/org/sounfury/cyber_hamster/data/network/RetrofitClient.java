package org.sounfury.cyber_hamster.data.network;

import org.sounfury.cyber_hamster.data.network.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.170.57:8081/";
    private static final int CONNECT_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    
    private static RetrofitClient instance;
    private final Retrofit retrofit;
    
    private RetrofitClient() {
        // 创建OkHttpClient
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        
        // 添加日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(loggingInterceptor);
        
        // 设置超时时间
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        
        // 创建Retrofit实例
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
    }
    
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    
    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
} 