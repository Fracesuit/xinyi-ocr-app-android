package com.xinyi_tech.comm.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import com.classic.common.MultipleStatusView;
import com.xinyi_tech.comm.help.EmptyLayoutHelp;

/**
 * Created by Fracesuit on 2017/11/6.
 */

public abstract class MultipleStatusActivity<P extends BasePresenter> extends BaseActivity<P> implements View.OnClickListener {
    public static final int NET_DATA_INIT = 1001;

    //net
    MultipleStatusView statusView;

    @Override
    @CallSuper
    protected void onCreateAfter(Bundle savedInstanceState) {
        statusView = getMultipleStatusView();
    }

    protected abstract MultipleStatusView getMultipleStatusView();


    @Override
    public void doOnStart(int requestCode) {
        if (requestCode == NET_DATA_INIT) {
            EmptyLayoutHelp.showLoading(statusView);
        } else {
            super.doOnStart(requestCode);
        }
    }


    @Override
    public void doOnError(int requestCode, String msg, Throwable e) {
        if (requestCode == NET_DATA_INIT) {
            EmptyLayoutHelp.showError(statusView, this);
        }
        super.doOnError(requestCode, msg, e);
    }

    @Override
    public void doOnCompleted(int requestCode) {
        if (requestCode == NET_DATA_INIT) {
            EmptyLayoutHelp.showContent(statusView);
        } else {
            super.doOnCompleted(requestCode);
        }
    }


    @Override
    public void onClick(View v) {

    }
}
