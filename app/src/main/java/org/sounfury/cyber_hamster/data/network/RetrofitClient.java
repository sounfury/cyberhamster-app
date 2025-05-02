package org.sounfury.cyber_hamster.data.network;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.network.Interceptor.TokenInterceptor;
import org.sounfury.cyber_hamster.data.network.api.BookService;
import org.sounfury.cyber_hamster.data.network.api.CategoryService;
import org.sounfury.cyber_hamster.data.network.api.NoteService;
import org.sounfury.cyber_hamster.data.network.api.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL = "http://192.168.1.15:8082/";
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
        // 添加Token拦截器
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        httpClientBuilder.addInterceptor(tokenInterceptor);
        
        // 设置超时时间
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        
        // 创建Retrofit实例
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();
    }
    
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    
    public UserService getApiService() {
        return retrofit.create(UserService.class);
    }

    public NoteService getNoteService() {
        return retrofit.create(NoteService.class);
    }

    public BookService getBookService() {
        return retrofit.create(BookService.class);
    }

    public CategoryService getCategoryService() {
        return retrofit.create(CategoryService.class);
    }
} 