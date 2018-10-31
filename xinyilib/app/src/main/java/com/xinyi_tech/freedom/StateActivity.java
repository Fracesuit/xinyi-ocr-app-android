package com.xinyi_tech.freedom;

import android.os.Bundle;
import android.view.View;

import com.classic.common.MultipleStatusView;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.help.EmptyLayoutHelp;
import com.xinyi_tech.comm.util.ToastyUtil;

import butterknife.BindView;

public class StateActivity extends BaseActivity<BasePresenter> {

    /* @BindView(R.id.stateful)
     StatefulLayout statefulLayout;*/
    @BindView(R.id.multiple_status_view)
    MultipleStatusView statefulLayout;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        EmptyLayoutHelp.showLoading(statefulLayout);
        statefulLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmptyLayoutHelp.showContent(statefulLayout);
            }
        }, 3000);
        statefulLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmptyLayoutHelp.showEmpty(statefulLayout);
            }
        }, 6000);
        statefulLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmptyLayoutHelp.showError(statefulLayout);

            }
        }, 9000);

        statefulLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                statefulLayout.showNoNetwork();
            }
        }, 12000);
        statefulLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmptyLayoutHelp.showContent(statefulLayout);
            }
        }, 15000);

        statefulLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastyUtil.warningShort("ddd");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_state;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }

    @Override
    public void doParseData(int requestCode, Object data) {

    }
}
