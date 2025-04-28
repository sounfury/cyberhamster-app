package org.sounfury.cyber_hamster.data.network.api;

import org.sounfury.cyber_hamster.data.network.request.LoginRequest;
import org.sounfury.cyber_hamster.data.network.request.RegisterRequest;
import org.sounfury.cyber_hamster.data.network.response.LoginResponse;
import org.sounfury.cyber_hamster.data.network.response.Result;
import org.sounfury.cyber_hamster.data.network.response.UserInfoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API服务接口，定义了与服务器交互的所有网络请求
 */
public interface ApiService {
    /**
     * 用户登录
     *
     * @param request 登录请求参数，包含用户名和密码
     * @return 登录响应，包含登录成功后的token和用户信息
     */
    @POST("/login")
    Call<Result<LoginResponse>> login(@Body LoginRequest request);

    /**
     * 用户注册
     *
     * @param request 注册请求参数，包含用户名、密码和邮箱
     * @return 注册响应
     */
    @POST("/register")
    Call<Result<Void>> register(@Body RegisterRequest request);

    /**
     * 获取用户信息
     *
     * @param token 用户身份验证token
     * @return 用户信息响应
     */
    @GET("/getInfo")
    Call<Result<UserInfoResponse>> getUserInfo(@Header("token") String token);

    /**
     * 用户登出
     *
     * @param token 用户身份验证token
     * @return 登出响应
     */
    @POST("/logout")
    Call<Result<Void>> logout(@Header("token") String token);

    /**
     * 检查用户名是否可用
     *
     * @param username 待检查的用户名
     * @return 用户名是否可用
     */
    @GET("/check-username")
    Call<Result<Boolean>> checkUsername(@Query("username") String username);

    /**
     * 修改密码
     *
     * @param newPassword 新密码
     * @param token 用户身份验证token
     * @return 修改密码响应
     */
    @PUT("/changePassword")
    Call<Result<Void>> changePassword(
            @Query("newPassword") String newPassword,
            @Header("token") String token);
} 