package com.xinyi_tech.freedom.myview.weiget.recycleview;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.BasePresenter;

public class RecycleViewActivity extends BaseListActivity<BasePresenter> {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
    }*/

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected BaseListFragment getFragment() {
        return new RecycleViewFragment();
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }
}
