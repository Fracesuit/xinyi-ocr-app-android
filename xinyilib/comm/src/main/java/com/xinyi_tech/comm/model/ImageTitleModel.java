package com.xinyi_tech.comm.model;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ImageTitleModel implements Comparable<ImageTitleModel>, Serializable, MultiItemEntity {
    private int drawableId;
    private String title;
    private int budgeCount;//提醒的数量
    private int orderIndex;//排序
    private Class clazz;
    private boolean isVisible = true;
    private String uniquenessId;//唯一标示性id
    private String iconPath;//图片路径
    private Object data;//方便携带数据
    private int itemType;//类型

    public Object getData() {
        return data;
    }

    public ImageTitleModel setData(Object data) {
        this.data = data;
        return this;
    }

    public ImageTitleModel(int drawableId, String title) {
        this(drawableId, title, 0, 0);
    }

    public ImageTitleModel(int drawableId, String title, int budgeCount) {
        this(drawableId, title, 0, budgeCount);
    }

    public ImageTitleModel(int drawableId, String title, int orderIndex, int budgeCount) {
        this.drawableId = drawableId;
        this.title = title;
        this.budgeCount = budgeCount;
        this.orderIndex = orderIndex;
    }

    public String getUniquenessId() {
        return uniquenessId;
    }

    public ImageTitleModel setUniquenessId(String uniquenessId) {
        this.uniquenessId = uniquenessId;
        return this;
    }

    public String getIconPath() {
        return iconPath;
    }

    public ImageTitleModel setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }

    @Override
    public int compareTo(@NonNull ImageTitleModel o) {
        return this.orderIndex - o.orderIndex;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public ImageTitleModel setDrawableId(int drawableId) {
        this.drawableId = drawableId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ImageTitleModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getBudgeCount() {
        return budgeCount;
    }

    public ImageTitleModel setBudgeCount(int budgeCount) {
        this.budgeCount = budgeCount;
        return this;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public ImageTitleModel setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public Class getClazz() {
        return clazz;
    }

    public ImageTitleModel setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public ImageTitleModel setVisible(boolean visible) {
        isVisible = visible;
        return this;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public ImageTitleModel setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

}
