package org.sounfury.cyber_hamster.data;

import org.sounfury.cyber_hamster.data.network.response.UserInfoResponse;

public class UserManager {

    private static volatile UserManager instance;

    private String token;          // 登录令牌
    private UserInfoResponse userInfo;     // 用户信息对象

    // 私有构造函数，禁止外部直接 new
    private UserManager() {
    }

    // 获取单例实例（双重检查锁，保证线程安全）
    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    // 设置 token
    public void setToken(String token) {
        this.token = token;
    }

    // 获取 token
    public String getToken() {
        return token;
    }

    // 设置用户信息
    public void setUserInfo(UserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }

    // 获取用户信息
    public UserInfoResponse getUserInfo() {
        return userInfo;
    }

    // 清除所有用户信息（退出登录时调用）
    public void clear() {
        token = null;
        userInfo = null;
    }
}