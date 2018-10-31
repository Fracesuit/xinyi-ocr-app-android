package com.xinyi_tech.comm.model;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyi_tech.comm.adapter.TreeAdapter;

import java.io.Serializable;

/**
 * Created by zhiren.zhang on 2016/12/12.
 */
//用来查看的
//用来选择的
//单选的  //多选的
public class TreeModel extends AbstractExpandableItem<TreeModel> implements MultiItemEntity, Serializable {
    private int iconId = -1;
    private boolean isSelected = false;//是否选中了
    int itemType = TreeAdapter.TYPE_LEVEL_NORMAL;//不同的类型加载不通的布局
    private String text;//显示的文字  必须
    private String value;//隐藏的value
    private Object data;
    private TreeModel parent;//父类对象

    //常规
    public TreeModel(TreeModel parent, String text) {
        this(parent, text, TreeAdapter.TYPE_LEVEL_NORMAL);
    }

    public TreeModel(TreeModel parent, String text, int itemType) {
        this.parent = parent;
        this.text = text;
        this.itemType = itemType;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public int getIconId() {
        return iconId;
    }

    public TreeModel setIconId(int iconId) {
        this.iconId = iconId;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public TreeModel setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    public TreeModel setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getText() {
        return text;
    }

    public TreeModel setText(String text) {
        this.text = text;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TreeModel setValue(String value) {
        this.value = value;
        return this;
    }

    public Object getData() {
        return data;
    }

    public TreeModel setData(Object data) {
        this.data = data;
        return this;
    }

    public TreeModel getParent() {
        return parent;
    }

    public TreeModel setParent(TreeModel parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getIndex() {
        return getCurrentIndex(this.getParent(), 0);
    }

    private int getCurrentIndex(TreeModel parent, int index) {
        if (parent != null) {
            index = getCurrentIndex(parent.getParent(), ++index);
        }
        return index;
    }
}
