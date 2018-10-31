package com.xinyi_tech.freedom.baserecycleviewadapterhelper;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/9/6.
 */

public class HelperAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public HelperAdapter() {
        super(R.layout.item_base_adapter);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.ctv_hourse_name,item);
    }

    @Override
    public void setNewData(@Nullable List<String> data) {
        List<String> data1 = getData();
        //data1=data;
        data1.add("dddd");
        //this.mData = data == null ? new ArrayList<String>() : data;
        this.mData=data;//.add("hhhh");
        notifyDataSetChanged();
    }
}
