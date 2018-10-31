package com.xinyi_tech.comm;

import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.xinyi_tech.comm.net.retrofit2.config.RetrofitManager;

/**
 * Created by zhiren.zhang on 2017/9/20.
 */
public abstract class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication";
    private boolean isDebug;
    protected RetrofitManager mRetrofitManager;

    public BaseApplication(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    public void onCreate() {
        CommInit.init(this, isDebug);
        super.onCreate();
    }

    protected <T> T createApiService(@NonNull RetrofitManager.Builder builder, Class<T> clazz) {
        //网络
        mRetrofitManager = builder.isDebug(isDebug).build();
        return mRetrofitManager.createApiService(clazz);
    }

    public RetrofitManager getRetrofitManager() {
        return mRetrofitManager;
    }
}
