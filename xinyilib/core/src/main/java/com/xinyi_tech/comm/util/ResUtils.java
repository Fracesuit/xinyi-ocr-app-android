package com.xinyi_tech.comm.util;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.Utils;

import java.util.Random;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ResUtils {
    //获取资源文件夹的方法
    public static Resources getResource() {
        return Utils.getApp().getResources();
    }

    //获取string操作
    public static String getString(int id) {
        return getResource().getString(id);
    }

    //获取string数组操作
    public static String[] getStringArray(int id) {
        return getResource().getStringArray(id);
    }

    //获取string数组操作
    //android.R.attr.colorAccent
    public static int getSysColor(int id) {
        int defaultColor = 0xFF000000;
        int[] attrsArray = {id};
        TypedArray typedArray = Utils.getApp().obtainStyledAttributes(attrsArray);
        int accentColor = typedArray.getColor(0, defaultColor);
        typedArray.recycle();
        return accentColor;
    }

    //android.R.attr.colorAccent
    public static int getColor(@ColorRes int color) {
        return ContextCompat.getColor(Utils.getApp(), color);
    }


}
