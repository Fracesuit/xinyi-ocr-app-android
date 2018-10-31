package com.xinyi_tech.comm.net.retrofit2.config;


import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.xinyi_tech.comm.net.retrofit2.config.converter.NullOnEmptyConverterFactory;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Fracesuit on 2017/5/22.
 */

public class RetrofitManager {
    private OkHttpClient mOkHttpClient;
    private Retrofit retrofit;

    private boolean isWatch;//是否监听
    private boolean isDebug;//是否是debug模式
    private boolean isHttps;//
    private int connectTimeout = 30;
    private int writeTimeout = 20;
    private int readTimeout = 20;
    private String host;
    private String port;
    private String domainName;
    private OkHttpClient.Builder okBuilder;

    private RetrofitManager(Builder builder) {
        isWatch = builder.isWatch;
        isDebug = builder.isDebug;
        isHttps = builder.isHttps;
        connectTimeout = builder.connectTimeout;
        writeTimeout = builder.writeTimeout;
        readTimeout = builder.readTimeout;
        host = builder.host;
        port = builder.port;
        domainName = builder.domainName;
        okBuilder = builder.okBuilder;

        initRetrofit();
    }

    public static Builder newBuilder(String host, String port) {
        return new Builder(host, port);
    }
    public static Builder newBuilder(String domainName) {
        return new Builder(domainName);
    }
    public <T> T createApiService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private void initRetrofit() {
        if (okBuilder == null) {
            okBuilder = new OkHttpClient.Builder();
        }
        okBuilder
                .retryOnConnectionFailure(true)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);

        //日志和调试
        if (isDebug) {
            Stetho.initializeWithDefaults(Utils.getApp());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            StethoInterceptor stethoInterceptor = new StethoInterceptor();
            okBuilder.addInterceptor(loggingInterceptor);
            okBuilder.addNetworkInterceptor(stethoInterceptor);
        }

        //加入进度监听
        if (isWatch) {
            okBuilder = ProgressManager.getInstance().with(okBuilder);
        }
        mOkHttpClient = okBuilder.build();

        String base_url =( isHttps ? "https" : "http" )+ "://" + (domainName == null ? (host + ":" + port) : domainName);
       // XinYiLog.e("base_url=="+base_url);
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(mOkHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public static final class Builder {
        private boolean isWatch = false;
        private boolean isDebug;
        private boolean isHttps;
        private int connectTimeout = 30;
        private int writeTimeout = 20;
        private int readTimeout = 20;
        private String host;
        private String port;
        private String domainName;
        private OkHttpClient.Builder okBuilder;

        private Builder(String host, String port) {
            this.host = host;
            this.port = port;
        }

        private Builder(String domainName) {
            this.domainName = domainName;
        }

        public Builder isWatch(boolean val) {
            isWatch = val;
            return this;
        }

        public Builder isDebug(boolean val) {
            isDebug = val;
            return this;
        }

        public Builder isHttps(boolean val) {
            isHttps = val;
            return this;
        }

        public Builder connectTimeout(int val) {
            connectTimeout = val;
            return this;
        }

        public Builder writeTimeout(int val) {
            writeTimeout = val;
            return this;
        }

        public Builder readTimeout(int val) {
            readTimeout = val;
            return this;
        }

        public Builder host(String val) {
            host = val;
            return this;
        }

        public Builder port(String val) {
            port = val;
            return this;
        }

        public Builder okBuilder(OkHttpClient.Builder val) {
            okBuilder = val;
            return this;
        }

        public RetrofitManager build() {
            return new RetrofitManager(this);
        }
    }
}
