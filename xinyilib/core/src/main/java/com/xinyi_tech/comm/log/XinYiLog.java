package com.xinyi_tech.comm.log;

import com.orhanobut.logger.Logger;

/**
 * Created by AppleRen on 2017/3/30.
 */

public class XinYiLog {

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void json(String json) {
        Logger.json(json);
    }
}
