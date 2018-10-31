package com.xinyi_tech.comm.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by zhiren.zhang on 2017/9/29.
 */

public class ActivityUtils2 {
    public static void startActivity(AppCompatActivity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static void startActivityByContext(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
    }


    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        return getTopActivity();
    }


    public static Activity getTopActivity() {
        Activity topActivity = null;
        try {
            topActivity = ActivityUtils.getTopActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topActivity;
    }

}
