package com.xinyi_tech.comm.rxjava1;


import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.base.BaseView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by AppleRen on 2017/3/27.
 */

public abstract class DefaultSubscriber<T> extends Subscriber<T> {
    BaseView mView;
    int mRequestCode;

    public DefaultSubscriber(@NonNull BaseView view, int requestCode) {
        mView = view;
        mRequestCode = requestCode;
    }

    public void doOnStart()//开始
    {
        mView.doOnStart(mRequestCode);
    }


    public LifecycleTransformer<T> bindLifecycle()//绑定生命周期,获取泛型的实际类型
    {
        return mView.doBindLifecycle(mRequestCode);
    }

    public void doOnError(Throwable e, String msg)//错误的时候
    {
        LogUtils.e(e.getMessage());
        mView.doOnError(mRequestCode, msg, e);
    }



    @Override
    public void onCompleted() {
        mView.doOnCompleted(mRequestCode);
    }


    //Subscriber 的onstart不能更改线程，所以需要重写
    @Override
    public final void onError(Throwable e) {
        String error = null;
        if (e instanceof ConnectException) {
            error = Utils.getApp().getString(R.string.comm_net_error_connent);
        } else if (e instanceof SocketTimeoutException) {
            error = Utils.getApp().getString(R.string.comm_net_error_timeout);
        } else if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 500:
                    error = Utils.getApp().getString(R.string.comm_net_error_service_busy);
                    break;
                case 530:
                    error = Utils.getApp().getString(R.string.comm_net_error_login);
                    break;
                case 404:
                    error = Utils.getApp().getString(R.string.comm_net_error_nourl);
                    break;
                default:
                    error = Utils.getApp().getString(R.string.comm_net_error_http) + ((HttpException) e).code();
                    break;
            }
        } else {
            error = e.getMessage();
        }
        doOnError(e, error);
    }
}
