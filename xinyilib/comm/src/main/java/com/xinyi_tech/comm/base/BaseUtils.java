package com.xinyi_tech.comm.base;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by zhiren.zhang on 2018/3/23.
 */

public class BaseUtils {

    static MaterialDialog dialog;

    public static void showDialog(AppCompatActivity activity, String content, DialogInterface.OnCancelListener cancelListener) {
        dialog = new MaterialDialog.Builder(activity)
                .content(content)
                .cancelable(cancelListener != null)
                .cancelListener(cancelListener)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .build();

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
