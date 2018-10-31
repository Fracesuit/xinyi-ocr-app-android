package com.xinyi_tech.freedom.xinyiduty;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.xinyi_tech.comm.base.BaseListActivity;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.util.ToastyUtil;
import com.xinyi_tech.comm.util.ToolbarUtils;
import com.xinyi_tech.freedom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class XinyiDutyListActivity extends BaseListActivity<XinyiDutyPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    private XinyiDutyListFragment mXinyiDutyListFragment;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        ToolbarUtils.with(this, mToolbar).setTitle("日志列表", true).build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xinyi_duty_list;
    }

    @Override
    protected XinyiDutyPresenter getPresenter() {
        return new XinyiDutyPresenter();
    }

    @Override
    protected BaseListFragment getFragment() {
        mXinyiDutyListFragment = new XinyiDutyListFragment();
        return mXinyiDutyListFragment;
    }

    @Override
    protected boolean isShowToolbar() {
        return true;
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        List<DutyModel> noTianDuty = mXinyiDutyListFragment.getNoTianDuty();
        if (noTianDuty.size() == 0) {
            ToastyUtil.warningShort("没有需要填写的日字");
            return;
        }
        mPresenter.saveDaily(100, noTianDuty);
    }
}
