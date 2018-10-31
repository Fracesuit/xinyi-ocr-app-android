package com.xinyi_tech.freedom.xinyiduty;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/5/7.
 */

public class XinyiDutyAdapter extends BaseQuickAdapter<DutyModel, BaseViewHolder> {
    public XinyiDutyAdapter() {
        super(R.layout.item_xinyi_duty);

    }

    @Override
    protected void convert(BaseViewHolder helper, DutyModel item) {
        SuperTextView view = helper.getView(R.id.stv_duty);
        view.setLeftString(item.getProjectname())
                .setLeftBottomString(item.getDutyTime() + "(" + item.getWeekChinese() + ")")
                .setLeftTextColor(ResUtils.getColor(item.isNeedInput() ? R.color.comm_black : R.color.comm_grey300))
                .setRightTopTextColor(ResUtils.getColor(!StringUtils.isEmpty(item.getDetail()) ? R.color.color_green : R.color.comm_red))
                .setRightTopString(StringUtils.isEmpty(item.getDetail()) ? "未填" : "已填")
        ;

    }



    public List<DutyModel> getNoTianDuty() {
        List<DutyModel> data = getData();
        ArrayList<DutyModel> dutyModels = new ArrayList<>();
        for (DutyModel dutyModel : data) {
            if (StringUtils.isEmpty(dutyModel.getDetail()) && dutyModel.isNeedInput()) {
                dutyModel.setDetail("界面功能优化");
                dutyModel.setCosttime("7.5");
                dutyModels.add(dutyModel);
            }
        }
        return dutyModels;

    }
}
