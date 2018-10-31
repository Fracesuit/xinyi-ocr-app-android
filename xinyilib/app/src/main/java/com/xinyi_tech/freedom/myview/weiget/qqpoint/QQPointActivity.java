package com.xinyi_tech.freedom.myview.weiget.qqpoint;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;

public class QQPointActivity extends BaseListActivity<BasePresenter> {

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqpoint);
    }*/

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected BaseListFragment getFragment() {
        return new QQPointFragment();
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }
}
