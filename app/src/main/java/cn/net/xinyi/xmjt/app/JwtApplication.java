package cn.net.xinyi.xmjt.app;

import com.xinyi_tech.comm.BaseApplication;
import com.xinyi_tech.comm.net.retrofit2.config.RetrofitManager;

import cn.net.xinyi.xmjt.BuildConfig;

/**
 * Created by zhiren.zhang on 2018/10/18.
 */

public class JwtApplication extends BaseApplication {

    private static ApiService apiService;

    public JwtApplication() {
        super(BuildConfig.ISDEBUG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //网络
        apiService = createApiService(RetrofitManager.newBuilder(BuildConfig.HOST, String.valueOf(BuildConfig.PORT)), ApiService.class);
    }

    //获取网络引擎
    public static ApiService getApiService() {
        return apiService;
    }

}
