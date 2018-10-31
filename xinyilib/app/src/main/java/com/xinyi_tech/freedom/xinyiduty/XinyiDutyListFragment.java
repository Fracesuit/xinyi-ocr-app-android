package com.xinyi_tech.freedom.xinyiduty;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyi_tech.comm.base.BaseListFragment;
import com.xinyi_tech.comm.base.ListSetupModel;

import java.util.List;

/**
 * Created by zhiren.zhang on 2018/5/7.
 */

public class XinyiDutyListFragment extends BaseListFragment<XinyiDutyAdapter, DutyModel, XinyiDutyPresenter> {

    @Override
    protected ListSetupModel setupParam() {
        return ListSetupModel.newBuilder().pageIndex(0).build();
    }

    @Override
    protected void onCreateViewAfter(View view, Bundle savedInstanceState) {
        super.onCreateViewAfter(view, savedInstanceState);
        baseListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.saveDaily(200, baseListAdapter.getItem(position));
                return true;
            }
        });
    }

    @Override
    protected XinyiDutyPresenter getPresenter() {
        return new XinyiDutyPresenter();
    }

    @Override
    protected XinyiDutyAdapter getAdapter() {
        return new XinyiDutyAdapter();
    }

    @Override
    protected void requestData(int requestCode, int pageIndex, int pageSize) {
        mPresenter.dutyList(requestCode, pageIndex, pageSize);
    }

    @Override
    protected void onItemClick(DutyModel dutyModel, int position) {
        if (StringUtils.isEmpty(dutyModel.getDetail())) {
            dutyModel.setNeedInput(!dutyModel.isNeedInput());
            baseListAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void doOnCompleted(int requestCode) {
        super.doOnCompleted(requestCode);
        if (requestCode == 200) {
            requestListData(REQUEST_STATE_REFRESH);
        }
    }

    public List<DutyModel> getNoTianDuty() {
        return baseListAdapter.getNoTianDuty();
    }
}
