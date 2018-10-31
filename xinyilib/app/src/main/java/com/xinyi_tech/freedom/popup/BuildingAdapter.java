package com.xinyi_tech.freedom.popup;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

import java.util.List;


/**
 * Created by Fracesuit on 2017/11/7.
 */

public class BuildingAdapter extends BaseQuickAdapter<BuildingModel, BaseViewHolder> {
    public BuildingAdapter() {
        super(R.layout.item_hourse);
    }

    @Override
    protected void convert(final BaseViewHolder helper, BuildingModel item) {
        final int adapterPosition = helper.getAdapterPosition();
        XinYiLog.e("BuildingAdapter==" + adapterPosition);
        final TextView hourse_name = helper.getView(R.id.ctv_hourse_name);
        hourse_name.setText(item.getBuildingInfoName());
        final View view = helper.getView(R.id.ll_hourse_root);
        view.setBackgroundResource(item.isSelect() ? R.color.comm_white : R.color.comm_grey300);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<BuildingModel> data = getData();

                for (int i = 0, j = data.size(); i < j; i++) {
                    data.get(i).setSelect(adapterPosition == i);
                }

                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(BuildingAdapter.this, v, helper.getLayoutPosition() - getHeaderLayoutCount());
                }
                notifyDataSetChanged();
            }
        });
    }

    public BuildingModel getSelectModel() {
        for (BuildingModel m : getData()) {
            if (m.isSelect()) {
                return m;
            }

        }
        return null;
    }
}
