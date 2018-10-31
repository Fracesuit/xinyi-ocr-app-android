package com.xinyi_tech.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.TypedValue;

import com.xinyi_tech.comm.util.ResUtils;

/**
 * Created by zhiren.zhang on 2018/6/20.
 */

public class FieldCheckBox extends AppCompatCheckBox {
    Object value;


    public FieldCheckBox(Context context, String text, String value, FieldView.Builder builder) {
        super(context);
        setText(text);
        if (builder != null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.getValueTextSize());
            setTextColor(ResUtils.getColor(builder.getValueTextColor()));
        }
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
