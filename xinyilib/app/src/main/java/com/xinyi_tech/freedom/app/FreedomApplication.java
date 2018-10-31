package com.xinyi_tech.freedom.app;

import com.xinyi_tech.comm.BaseApplication;
import com.xinyi_tech.comm.net.retrofit2.config.RetrofitManager;

import okhttp3.OkHttpClient;

/**
 * Created by zhiren.zhang on 2018/1/22.
 */

public class FreedomApplication extends BaseApplication {
    public static Api apiService;

    public FreedomApplication() {
        super(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //网络  lggafw.com/    192.168.21.59  77f.tech :8080
        OkHttpClient.Builder okHttpClientBuilder = Okhttp3Help.getOkHttpClientBuilder();
        //  okHttpClientBuilder.cookieJar(new CookieJar(new SPCookieStore(this)))
        final RetrofitManager.Builder builder = RetrofitManager.newBuilder(
                "183.62.140.7",
                "8088")
                .isHttps(false)
                .connectTimeout(60)
                .writeTimeout(60)
                .okBuilder(okHttpClientBuilder)
                .readTimeout(60);
        apiService = createApiService(builder, Api.class);
    }
}
