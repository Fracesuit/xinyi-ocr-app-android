package com.xinyi_tech.comm.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.model.KeyValueModel;


/**
 * Created by Fracesuit on 2017/7/21.
 */

public class KeyValueAdapter extends BaseQuickAdapter<KeyValueModel, BaseViewHolder> {

    public KeyValueAdapter() {
        super(R.layout.comm_item_key_value);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final KeyValueModel item) {
        helper.setText(R.id.tv_inspect_item_key, item.key);
        helper.setText(R.id.tv_inspect_item_value, item.value);

        ImageView iconView = helper.getView(R.id.ic_icon);
        iconView.setImageResource(item.iconRes);
        helper.setGone(R.id.ic_icon, item.iconRes != 0);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIconClickListener != null) {
                    onIconClickListener.onIconClick(item);
                } else {
                    helper.itemView.performClick();
                }

            }
        });
    }

    public interface OnIconClickListener {
        void onIconClick(KeyValueModel item);
    }

    OnIconClickListener onIconClickListener;

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }
}
