package com.xinyi_tech.freedom.myview.weiget.channel;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.freedom.R;

import java.util.List;

/**
 * Created by zhiren.zhang on 2018/5/8.
 */

public class ChannelAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public ChannelAdapter() {
        super(null);
        addItemType(0, R.layout.item_channel);
        addItemType(1, R.layout.item_channel_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case 0:
                ChannelModel channelModel = (ChannelModel) item;
                helper.setVisible(R.id.img_delete, channelModel.isSelect());
                helper.setText(R.id.tv_channel_name, channelModel.getChannelName());
                break;
            case 1:
                GroupTextModel model = (GroupTextModel) item;
                helper.setText(R.id.tv_channel_group, model.getGroupText());
                break;
        }
    }

    public int getSelectedCount() {
        //选择的数量
        int selectedCount = 0;
        List<MultiItemEntity> data = getData();
        for (MultiItemEntity m : data) {
            if (0 == m.getItemType()) {
                ChannelModel channelModel = (ChannelModel) m;
                if (channelModel.isSelect()) {
                    selectedCount++;
                }
            }
        }
        return selectedCount;
    }


}
