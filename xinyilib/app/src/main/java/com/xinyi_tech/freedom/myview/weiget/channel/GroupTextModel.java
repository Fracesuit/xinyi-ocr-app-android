package com.xinyi_tech.freedom.myview.weiget.channel;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhiren.zhang on 2018/5/8.
 */

public class GroupTextModel implements MultiItemEntity {

    private String groupText;

    public GroupTextModel(String groupText) {
        this.groupText = groupText;
    }

    public String getGroupText() {
        return groupText;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
