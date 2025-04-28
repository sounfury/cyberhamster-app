package org.sounfury.cyber_hamster.data.network.response;

import java.util.Map;

public class LoginResponse {
   private String token;
    // 辅助方法，方便获取token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}