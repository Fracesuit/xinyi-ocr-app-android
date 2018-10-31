package com.xinyi_tech.freedom.tree;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;

public class TreeActivity extends BaseListActivity<BasePresenter> {

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    protected BaseListFragment getFragment() {
        return new TreeFragment();
    }


    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }
}
