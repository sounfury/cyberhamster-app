package org.sounfury.cyber_hamster.data.network.Interceptor;// TokenInterceptor.java
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.sounfury.cyber_hamster.data.UserManager;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取原本的请求
        Request originalRequest = chain.request();

        // 取出保存在本地的token
        String token = UserManager.getInstance().getToken();

        // 如果token为空，直接走原请求
        if (token == null || token.isEmpty()) {
            return chain.proceed(originalRequest);
        }

        // 重新构建请求，加上token
        Request newRequest = originalRequest.newBuilder()
                .header("token", token) // 统一添加token
                .build();

        return chain.proceed(newRequest);
    }
}
