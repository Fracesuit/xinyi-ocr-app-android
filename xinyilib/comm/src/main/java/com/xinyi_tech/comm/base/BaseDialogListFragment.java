package com.xinyi_tech.comm.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import com.blankj.utilcode.util.FragmentUtils;
import com.xinyi_tech.comm.R;


/**
 * Created by Fracesuit on 2017/8/7.
 */

public abstract class BaseDialogListFragment<P extends BasePresenter> extends BaseDialogFragment<P> {
    protected BaseListFragment fragment;

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        fragment = getFragment();
        FragmentUtils.add(activity.getSupportFragmentManager(), fragment, R.id.fl_list_content);
    }

    protected abstract BaseListFragment getFragment();

    @Override
    protected int getLayoutId() {
        return R.layout.comm_activity_baselist;
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }
}
