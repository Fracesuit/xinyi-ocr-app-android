package com.xinyi_tech.freedom.myview.weiget.channel;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhiren.zhang on 2018/5/8.
 */

public class ChannelModel implements MultiItemEntity {

    boolean select;
    String channelName;

    public ChannelModel(boolean select, String channelName) {
        this.select = select;
        this.channelName = channelName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
