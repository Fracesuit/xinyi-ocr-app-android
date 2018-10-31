package com.xinyi_tech.freedom.popup;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

import java.util.List;


/**
 * Created by Fracesuit on 2017/11/7.
 */

public class HourseAdapter extends BaseQuickAdapter<HourseModel, BaseViewHolder> {
    public HourseAdapter() {
        super(R.layout.item_room);
    }

    @Override
    protected void convert(final BaseViewHolder helper, HourseModel item) {
        final View view = helper.getView(R.id.ll_room_root);
        final TextView tv_room_name = helper.getView(R.id.tv_room_name);
        tv_room_name.setText(item.getHourseName());
        final TextView tv_pnum = helper.getView(R.id.tv_pnum);
        tv_pnum.setText(item.getHoursePersonNum());
        tv_room_name.setTextColor(item.isSelect() ? ResUtils.getColor(R.color.comm_orange) : ResUtils.getColor(R.color.comm_grey900));
        tv_pnum.setTextColor(item.isSelect() ? ResUtils.getColor(R.color.comm_orange) : ResUtils.getColor(R.color.comm_grey900));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<HourseModel> data = getData();
                final int adapterPosition = helper.getAdapterPosition();
                for (int i = 0, j = data.size(); i < j; i++) {
                    data.get(i).setSelect(adapterPosition == i);
                }

                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(HourseAdapter.this, v, helper.getLayoutPosition() - getHeaderLayoutCount());
                }

                notifyDataSetChanged();

            }
        });
    }

    public HourseModel getSelectModel() {
        for (HourseModel m : getData()) {
            if (m.isSelect()) {
                return m;
            }

        }
        return null;
    }
}
