package org.sounfury.cyber_hamster.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.sounfury.cyber_hamster.data.model.User;
import org.sounfury.cyber_hamster.data.network.RetrofitClient;
import org.sounfury.cyber_hamster.data.network.api.ApiService;
import org.sounfury.cyber_hamster.data.network.request.LoginRequest;
import org.sounfury.cyber_hamster.data.network.request.RegisterRequest;
import org.sounfury.cyber_hamster.data.network.response.LoginResponse;
import org.sounfury.cyber_hamster.data.network.response.Result;
import org.sounfury.cyber_hamster.data.network.response.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_TOKEN = "user_token";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PASSWORD = "user_password";
    private static final String KEY_REMEMBER = "remember_password";

    private static UserRepository instance;
    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private String token;

    private UserRepository(Context context) {
        apiService = RetrofitClient.getInstance().getApiService();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        loading.setValue(false);
    }

    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public String getToken() {
        return token;
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(token);
    }

    /**
     *  自动加载保存的用户名和密码
     */
    public void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (rememberMe) {
            String username = sharedPreferences.getString(KEY_USERNAME, "");
            String password = sharedPreferences.getString(KEY_PASSWORD, "");
            
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                login(username, password, true);
            }
        }
    }

    public void login(String username, String password,boolean rememberMe) {
        loading.setValue(true);
        LoginRequest request = new LoginRequest(username, password);
        
        apiService.login(request).enqueue(new Callback<Result<LoginResponse>>() {
            @Override
            public void onResponse(Call<Result<LoginResponse>> call, Response<Result<LoginResponse>> response) {
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    Result<LoginResponse> result = response.body();

                    if (result.isSuccess() && result.getData() != null) {
                        String newToken = result.getData().getToken();
                        if (!TextUtils.isEmpty(newToken)) {
                            // 保存token
                            token = newToken;
                            saveToken(token);

                            // 如果选择了记住密码，则保存用户名和密码
                            if (rememberMe) {
                                saveCredentials(username, password, true);
                            } else {
                                clearCredentials();
                            }

                            // 获取用户信息
                            fetchUserInfo();
                        } else {
                            errorMessage.setValue("登录失败：Token为空");
                        }
                    } else {
                        errorMessage.setValue("登录失败：" + result.getMessage());
                    }
                } else {
                    errorMessage.setValue("登录失败：服务器错误");
                }
            }

            @Override
            public void onFailure(Call<Result<LoginResponse>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("登录失败：" + t.getMessage());
                Log.e(TAG, "Login error", t);
            }
        });
    }

    public void register(String username, String password, String email) {
        loading.setValue(true);
        RegisterRequest request = new RegisterRequest(username, password, email);
        
        apiService.register(request).enqueue(new Callback<Result<Void>>() {
            @Override
            public void onResponse(Call<Result<Void>> call, Response<Result<Void>> response) {
                loading.setValue(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    Result<Void> result = response.body();
                    
                    if (result.isSuccess()) {
                        // 注册成功，尝试登录
                        login(username, password, false);
                    } else {
                        errorMessage.setValue("注册失败：" + result.getMessage());
                    }
                } else {
                    errorMessage.setValue("注册失败：服务器错误");
                }
            }

            @Override
            public void onFailure(Call<Result<Void>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("注册失败：" + t.getMessage());
                Log.e(TAG, "Register error", t);
            }
        });
    }

    public void checkUsername(String username) {
        apiService.checkUsername(username).enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                // 处理用户名检查结果
                // 由于这只是一个检查操作，我们不需要在这里更改任何状态
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e(TAG, "Check username error", t);
            }
        });
    }

    private void fetchUserInfo() {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        
        loading.setValue(true);
        apiService.getUserInfo(token).enqueue(new Callback<Result<UserInfoResponse>>() {
            @Override
            public void onResponse(Call<Result<UserInfoResponse>> call, Response<Result<UserInfoResponse>> response) {
                loading.setValue(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    Result<UserInfoResponse> result = response.body();
                    
                    if (result.isSuccess() && result.getData() != null 
                            && result.getData() != null
                            && result.getData().getLoginUser() != null) {
                        
                        User user = result.getData().getLoginUser();
                        currentUser.setValue(user);
                    } else {
                        errorMessage.setValue("获取用户信息失败：" + result.getMessage());
                    }
                } else {
                    errorMessage.setValue("获取用户信息失败：服务器错误");
                }
            }

            @Override
            public void onFailure(Call<Result<UserInfoResponse>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("获取用户信息失败：" + t.getMessage());
                Log.e(TAG, "Fetch user info error", t);
            }
        });
    }

    public void logout() {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        
        loading.setValue(true);
        apiService.logout(token).enqueue(new Callback<Result<Void>>() {
            @Override
            public void onResponse(Call<Result<Void>> call, Response<Result<Void>> response) {
                loading.setValue(false);
                
                // 无论服务器返回成功与否，都清除本地登录状态
                clearToken();
                currentUser.setValue(null);
            }

            @Override
            public void onFailure(Call<Result<Void>> call, Throwable t) {
                loading.setValue(false);
                Log.e(TAG, "Logout error", t);
                
                // 即使请求失败，也清除本地登录状态
                clearToken();
                currentUser.setValue(null);
            }
        });
    }

    public void changePassword(Long userId, String newPassword) {
        if (TextUtils.isEmpty(token) || userId == null) {
            return;
        }
        
//        loading.setValue(true);
//        apiService.changePassword(userId, newPassword, token).enqueue(new Callback<Result<Void>>() {
//            @Override
//            public void onResponse(Call<Result<Void>> call, Response<Result<Void>> response) {
//                loading.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    Result<Void> result = response.body();
//
//                    if (result.isSuccess()) {
//                        // 密码修改成功，更新保存的密码（如果启用了记住密码）
//                        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
//                        if (rememberMe) {
//                            String username = sharedPreferences.getString(KEY_USERNAME, "");
//                            saveCredentials(username, newPassword, true);
//                        }
//                    } else {
//                        errorMessage.setValue("修改密码失败：" + result.getMessage());
//                    }
//                } else {
//                    errorMessage.setValue("修改密码失败：服务器错误");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Result<Void>> call, Throwable t) {
//                loading.setValue(false);
//                errorMessage.setValue("修改密码失败：" + t.getMessage());
//                Log.e(TAG, "Change password error", t);
//            }
//        });
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    private void clearToken() {
        token = "";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    private void saveCredentials(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, rememberMe);
        editor.apply();
    }

    private void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false);
        editor.apply();
    }
} 