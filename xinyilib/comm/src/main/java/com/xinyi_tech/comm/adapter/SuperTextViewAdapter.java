package com.xinyi_tech.comm.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.model.GroupModel;
import com.xinyi_tech.comm.model.KeyValueModel;


/**
 * Created by Fracesuit on 2017/7/21.
 */

public abstract class SuperTextViewAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public SuperTextViewAdapter() {
        super(null);
        addItemType(0, R.layout.comm_item_tree_group);
        addItemType(1, R.layout.comm_item_key_value);
        addItemType(2, R.layout.comm_item_super_text);
    }

    @Override
    public void addItemType(int type, @LayoutRes int layoutResId) {
        super.addItemType(type, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case 0:
                group(helper, item);
                break;
            case 1:
                keyValue(helper, item);
                break;
            case 2:
                superText(helper, item);
                break;
        }
    }

    private void group(BaseViewHolder helper, MultiItemEntity model) {
        GroupModel item = (GroupModel) model;
        AppCompatTextView tv_group = helper.getView(R.id.tv_group);
        tv_group.setText(item.getText());
    }

    private void keyValue(final BaseViewHolder helper, final MultiItemEntity model) {
        final KeyValueModel item = (KeyValueModel) model;
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

    protected abstract void superText(BaseViewHolder helper, MultiItemEntity item);

}
