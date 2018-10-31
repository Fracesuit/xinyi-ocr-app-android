package com.xinyi_tech.comm.widget.arrow;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;

import java.util.List;


/**
 * Created by Fracesuit on 2017/12/27.
 */

public class ArrowAdapter extends BaseQuickAdapter<ArrowModel, BaseViewHolder> {

    public ArrowAdapter(@Nullable List<ArrowModel> data) {
        super(R.layout.comm_item_arrow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArrowModel item) {
        helper.setText(R.id.tv_arrow_name, item.getName());
    }
}
