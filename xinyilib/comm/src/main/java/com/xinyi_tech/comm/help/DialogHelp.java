package com.xinyi_tech.comm.help;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by zhiren.zhang on 2017/9/21.
 */

public class DialogHelp {
    public static MaterialDialog createProgressDialog(AppCompatActivity activity, String content, DialogInterface.OnCancelListener cancelListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                // .title(R.string.comm_loading_wait_msg)
                .content(content)
                .cancelable(cancelListener != null)
                .cancelListener(cancelListener)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .build();
        return dialog;
    }
}
