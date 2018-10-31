package com.xinyi_tech.comm.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.blankj.utilcode.util.IntentUtils;
import com.example.core.R;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscriber;


/**
 * Created by Fracesuit on 2017/6/13.
 */

public class PermissionsHelp {
    //跳转到请求权限的请求码
    public static final int PERMISSION_REQUEST_CODE = 5678;

    /**
     * 权限列表
     */

    //存储
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //位置
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;//gps定位权限
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;//基站定位权限
    //相机
    public static final String CAMERA = Manifest.permission.CAMERA;
    //打电话
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    //联系人
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;


    private Activity activity;
    private RxPermissions rxPermissions;

    private PermissionsHelp(Activity activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
    }

    private PermissionsHelp() {
        throw new RuntimeException("必须使用带有一个参数的构造方法");
    }

    //这里是单例的话，会造成
    public static PermissionsHelp with(Activity activity) {
        PermissionsHelp permissionsHelp = null;
        if (permissionsHelp == null) {
            synchronized (PermissionsHelp.class) {
                if (permissionsHelp == null)
                    permissionsHelp = new PermissionsHelp(activity);
            }
        }
        return permissionsHelp;
    }

    public void requestPermissions(String... permissions) {
        requestPermissions(null, permissions);
    }

    public void requestPermissions(final OnRequestPermissionsListener onRequestPermissionsListener, String... permissions) {
        rxPermissions.request(permissions)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.completed();
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.error(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (onRequestPermissionsListener != null) {
                            if (granted) {
                                onRequestPermissionsListener.grant();
                            } else {
                                showPermissonDialog(onRequestPermissionsListener);
                            }
                        }

                    }
                });
    }

    private void showPermissonDialog(final OnRequestPermissionsListener onRequestPermissionsListener) {

        new AlertDialog.Builder(activity)
                .setMessage(R.string.comm_permission_message_permission_failed)
                .setTitle(R.string.comm_permission_title)
                .setCancelable(false)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent appDetailsSettingsIntent = IntentUtils.getLaunchAppDetailsSettingsIntent(activity.getPackageName());
                        //跳转到权限设置界面
                        activity.startActivityForResult(appDetailsSettingsIntent, PERMISSION_REQUEST_CODE);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.refuse();
                        }
                    }
                })
                .show();
    }
}
