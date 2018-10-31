package com.xinyi_tech.freedom.app;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zhiren.zhang on 2017/10/16.
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        // Request.Builder requestBuilder = original.newBuilder();
        //  Request request = requestBuilder.build();
        Response proceed = chain.proceed(original);
        if (original.url().toString().contains("saveDaily")) {
            return proceed.newBuilder().body(ResponseBody.create(MediaType.parse("text/html"), "1")).build();
        }
        return proceed;
    }
}
