package com.xinyi_tech.comm.util;

import android.support.v7.app.AppCompatActivity;

import com.xinyi_tech.comm.R;


/**
 * Created by Fracesuit on 2017/7/31.
 */

public class OverridePendingTransitionUtls {
    //新从右边进入  旧的不动
    public static void slideRightEntry(AppCompatActivity activity) {
        activity.overridePendingTransition(R.anim.comm_slide_right_entry, R.anim.comm_hold);
    }

    //旧的不动,新的从右边退出
    public static void slideRightExit(AppCompatActivity activity) {
        activity.overridePendingTransition(R.anim.comm_hold, R.anim.comm_slide_right_exit);
    }

    //放大进入
    public static void zoomInEntry(AppCompatActivity activity) {
        activity.overridePendingTransition(R.anim.comm_zoom_in_entry, R.anim.comm_hold);
    }

    //退出
    public static void zoomInExit(AppCompatActivity activity) {
        activity.overridePendingTransition(R.anim.comm_hold, R.anim.comm_zoom_in_exit);
    }


}
