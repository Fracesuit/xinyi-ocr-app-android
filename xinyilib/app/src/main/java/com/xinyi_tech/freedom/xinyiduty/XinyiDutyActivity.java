package com.xinyi_tech.freedom.xinyiduty;

import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.xinyi_tech.comm.base.BaseActivity;
import com.xinyi_tech.comm.form.FormLayout;
import com.xinyi_tech.freedom.R;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.HttpException;

public class XinyiDutyActivity extends BaseActivity<XinyiDutyPresenter> {

    @BindView(R.id.form)
    FormLayout mForm;


    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xinyi_duty;
    }

    @Override
    protected XinyiDutyPresenter getPresenter() {
        return new XinyiDutyPresenter();
    }

    @OnClick(R.id.btn_Login)
    public void onViewClicked() {
        mPresenter.login(mForm.getParams(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE));
    }

    @Override
    public void doOnError(int requestCode, String msg, Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (httpException.code() == 302) {
                ActivityUtils.finishActivity(this);
                ActivityUtils.startActivity(this, XinyiDutyListActivity.class);
                SPUtils.getInstance().put("login",true);
                return;
            }
        }
        super.doOnError(requestCode, msg, e);
    }
}
