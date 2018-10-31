package com.xinyi_tech.freedom.myview.weiget.recycleview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;

/**
 * Created by zhiren.zhang on 2018/5/16.
 */

public class RecycleViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecycleViewAdapter(ArrayList<String> strings) {

        super(R.layout.item_recycleview, strings);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_num, String.valueOf(helper.getAdapterPosition()));
    }
}
