package com.xinyi_tech.comm.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by zhiren.zhang on 2017/11/14.
 */

public class GroupModel implements MultiItemEntity, Serializable {
    private String text;

    public GroupModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
