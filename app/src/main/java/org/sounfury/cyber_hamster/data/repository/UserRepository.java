package org.sounfury.cyber_hamster.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.sounfury.cyber_hamster.data.UserManager;
import org.sounfury.cyber_hamster.data.model.User;
import org.sounfury.cyber_hamster.data.network.RetrofitClient;
import org.sounfury.cyber_hamster.data.network.api.UserService;
import org.sounfury.cyber_hamster.data.network.request.LoginRequest;
import org.sounfury.cyber_hamster.data.network.request.RegisterRequest;
import org.sounfury.cyber_hamster.data.network.request.ChangePasswordInput;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_TOKEN = "user_token";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PASSWORD = "user_password";
    private static final String KEY_REMEMBER = "remember_password";

    private static UserRepository instance;
    private final UserService userService;
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private String token;
    
    // 用于管理RxJava订阅
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private UserRepository(Context context) {
        userService = RetrofitClient.getInstance().getApiService();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        if(!TextUtils.isEmpty(token)) {
            UserManager.getInstance().setToken(token);
        }
        loading.postValue(false);
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
     * 检查Token和自动登录
     * @return 返回一个Observable，表示自动登录的结果：true表示成功，false表示需要手动登录
     */
    public Observable<Boolean> checkTokenAndAutoLogin() {
        // 首先检查token是否存在
        if (!TextUtils.isEmpty(token)) {
            // 如果token存在，尝试获取用户信息验证token有效性
            return fetchUserInfoRx()
                    .map(user -> {
                        // 如果获取用户信息成功，返回true表示可以自动登录
                        currentUser.postValue(user);
                        return true;
                    })
                    .onErrorReturn(throwable -> {
                        // 如果获取用户信息失败，清除token并返回false
                        Log.e(TAG, "Token无效，需要重新登录", throwable);
                        clearToken();
                        return false;
                    });
        } else {
            // 如果没有token，尝试通过保存的用户名和密码自动登录
            boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
            if (rememberMe) {
                String username = sharedPreferences.getString(KEY_USERNAME, "");
                String password = sharedPreferences.getString(KEY_PASSWORD, "");

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    return loginRx(username, password, true)
                            .map(user -> {
                                currentUser.postValue(user);
                                return true;
                            })
                            .onErrorReturn(throwable -> {
                                Log.e(TAG, "自动登录失败", throwable);
                                return false;
                            });
                }
            }
            // 没有保存的凭据或记住密码未开启
            return Observable.just(false);
        }
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

    public void login(String username, String password, boolean rememberMe) {
        loading.postValue(true);
        
        Disposable disposable = loginRx(username, password, rememberMe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            loading.postValue(false);
                            currentUser.postValue(user);
                        },
                        throwable -> {
                            loading.postValue(false);
                            errorMessage.postValue("登录失败：" + throwable.getMessage());
                            Log.e(TAG, "Login error", throwable);
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    private Observable<User> loginRx(String username, String password, boolean rememberMe) {
        LoginRequest request = new LoginRequest(username, password);
        //先清除token
        clearToken();
        
        return userService.login(request)
                .flatMap(result -> {
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
                            return fetchUserInfoRx();
                        } else {
                            return Observable.error(new Exception("登录失败：Token为空"));
                        }
                    } else {
                        return Observable.error(new Exception("登录失败：" + result.getMessage()));
                    }
                });
    }
    
    private Observable<User> fetchUserInfoRx() {
        if (TextUtils.isEmpty(token)) {
            return Observable.error(new Exception("Token为空，无法获取用户信息"));
        }
        
        return userService.getUserInfo()
                .map(result -> {
                    if (result.isSuccess() && result.getData() != null 
                            && result.getData().getLoginUser() != null) {
                        UserManager.getInstance().setUserInfo(result.getData());
                        return result.getData().getLoginUser();
                    } else {
                        throw new Exception("获取用户信息失败：" + result.getMessage());
                    }
                });
    }

    public void register(String username, String password, String email,String nickname) {
        loading.postValue(true);
        RegisterRequest request = new RegisterRequest(username, password, email,nickname);
        
        Disposable disposable = userService.register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(result -> {
                    if (result.isSuccess()) {
                        // 注册成功，尝试登录
                        return loginRx(username, password, false);
                    } else {
                        return Observable.error(new Exception("注册失败：" + result.getMessage()));
                    }
                })
                .subscribe(
                        user -> {
                            loading.postValue(false);
                            currentUser.postValue(user);
                        },
                        throwable -> {
                            loading.postValue(false);
                            errorMessage.postValue("注册失败：" + throwable.getMessage());
                            Log.e(TAG, "Register error", throwable);
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    public void checkUsername(String username) {
        Disposable disposable = userService.checkUsername(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            // 处理用户名检查结果
                            // 由于这只是一个检查操作，我们不需要在这里更改任何状态
                        },
                        throwable -> {
                            Log.e(TAG, "Check username error", throwable);
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    private void fetchUserInfo() {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        loading.postValue(true);
        
        Disposable disposable = fetchUserInfoRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            loading.postValue(false);
                            currentUser.postValue(user);
                        },
                        throwable -> {
                            loading.postValue(false);
                            errorMessage.postValue("获取用户信息失败：" + throwable.getMessage());
                            Log.e(TAG, "Fetch user info error", throwable);
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    public void logout() {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        loading.postValue(true);
        
        Disposable disposable = userService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            loading.postValue(false);
                            clearToken();
                            currentUser.postValue(null);
                        },
                        throwable -> {
                            loading.postValue(false);
                            Log.e(TAG, "Logout error", throwable);
                            // 即使请求失败，也清除本地登录状态
                            clearToken();
                            currentUser.postValue(null);
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        loading.postValue(true);
        
        ChangePasswordInput input = new ChangePasswordInput(oldPassword, newPassword);
        Disposable disposable = userService.changePassword(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            loading.postValue(false);
                            if (result.isSuccess()) {
                                // 密码修改成功
                                errorMessage.postValue("密码修改成功");
                            } else {
                                // 密码修改失败
                                errorMessage.postValue("密码修改失败：" + result.getMessage());
                            }
                        },
                        throwable -> {
                            loading.postValue(false);
                            errorMessage.postValue("密码修改失败：" + throwable.getMessage());
                            Log.e(TAG, "Change password error", throwable);
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
        Log.i(TAG, "Token saved: " + token);
        //全局存储
        UserManager.getInstance().setToken(token);
    }

    private void clearToken() {
        token = "";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
        UserManager.getInstance().setToken(token);
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
    
    /**
     * 清理资源，防止内存泄漏
     */
    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}