package com.xinyi_tech.comm.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.blankj.utilcode.util.FragmentUtils;
import com.xinyi_tech.comm.R;


/**
 * Created by Fracesuit on 2017/8/7.
 */

public abstract class BaseListActivity<P extends BasePresenter> extends BaseActivity<P> {
    protected BaseListFragment fragment;

    @Override
    protected int getLayoutId() {
        return isShowToolbar() ? R.layout.comm_activity_baselist_with_toolbar : R.layout.comm_activity_baselist;
    }

    @Override
    @CallSuper
    protected void onCreateAfter(Bundle savedInstanceState) {
        fragment = getFragment();
        FragmentUtils.add(getSupportFragmentManager(), fragment, R.id.fl_list_content);
    }

    protected abstract BaseListFragment getFragment();

    @Override
    public void doParseData(int requestCode, Object data) {

    }


    protected abstract boolean isShowToolbar();
}
