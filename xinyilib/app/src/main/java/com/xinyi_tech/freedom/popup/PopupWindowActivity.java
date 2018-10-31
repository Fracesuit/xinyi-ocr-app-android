package com.xinyi_tech.freedom.popup;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.common.MultipleStatusView;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.base.BasePresenter;
import com.xinyi_tech.comm.base.MultipleStatusActivity;
import com.xinyi_tech.freedom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/7/30.
 */

public class PopupWindowActivity extends MultipleStatusActivity<BasePresenter> {
    @BindView(R.id.tv_hourse_title)
    TextView tvHourseTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.view_state)
    MultipleStatusView viewState;

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        super.onCreateAfter(savedInstanceState);
        final TestPopupWindow p = new TestPopupWindow(this);
        tvHourseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.showAsDropDown(toolBar);
            }
        });


    }

    @Override
    protected MultipleStatusView getMultipleStatusView() {
        return viewState;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_popup;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BasePresenter();
    }
}
