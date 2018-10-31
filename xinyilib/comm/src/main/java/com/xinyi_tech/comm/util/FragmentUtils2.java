package com.xinyi_tech.comm.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by zhiren.zhang on 2017/10/18.
 */

public class FragmentUtils2 {

    public static void put(Fragment fragment, String key, Object value) {
        if (value == null) {
            return;
        }
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else {
            throw new RuntimeException("没有找到类型");
        }
        fragment.setArguments(bundle);
    }

    public static <T> T get(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            return (T) arguments.get(key);
        }
        return null;
    }
}
