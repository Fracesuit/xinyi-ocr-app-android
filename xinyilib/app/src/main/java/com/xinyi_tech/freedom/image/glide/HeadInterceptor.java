package com.xinyi_tech.freedom.image.glide;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhiren.zhang on 2017/10/16.
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.addHeader("x-auth-token", "glide");

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
