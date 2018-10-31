package com.xinyi_tech.comm.util;

import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

import es.dmoral.toasty.Toasty;

/**
 * Created by zhiren.zhang on 2017/9/22.
 */

public class ToastyUtil {

    public static void infoShort(String msg) {
        Toasty.info(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void infoLong(String msg) {
        Toasty.info(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void errorShort(String msg) {
        Toasty.error(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void errorLong(String msg) {
        Toasty.error(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void warningShort(String msg) {
        Toasty.warning(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void warningLong(String msg) {
        Toasty.warning(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void normalShort(String msg) {
        Toasty.normal(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void normalLong(String msg) {
        Toasty.normal(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void successShort(String msg) {
        Toasty.success(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void successLong(String msg) {
        Toasty.success(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }
}
