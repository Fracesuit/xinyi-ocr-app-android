package com.xinyi_tech.comm.picker.file;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;

public class FileSelectActivity extends BaseListActivity<BasePresenter> {
    private FileSelectFragment mFileSelectFragment;

    @Override
    protected BaseListFragment getFragment() {
        mFileSelectFragment = new FileSelectFragment();
        return mFileSelectFragment;
    }


    @Override
    public void onBackPressed() {
        if (mFileSelectFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }
}
