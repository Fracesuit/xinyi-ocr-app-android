package com.xinyi_tech.comm.model;

import android.support.annotation.DrawableRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/8/4.
 */

public class KeyValueModel implements Serializable, MultiItemEntity {
    public String key;
    public String value;
    public int iconRes;
    public Object data;

    public KeyValueModel(String key, String value) {
        this(key, value, 0);
    }

    public KeyValueModel(String key, String value, @DrawableRes int iconRes) {
        this.key = key;
        this.value = value;
        this.iconRes = iconRes;
    }

    public Object getData() {
        return data;
    }

    public KeyValueModel setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
