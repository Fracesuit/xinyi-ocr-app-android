package com.xinyi_tech.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.TypedValue;

import com.xinyi_tech.comm.util.ResUtils;

/**
 * Created by Fracesuit on 2017/8/8.
 */

public class FieldRadioButton extends AppCompatRadioButton {
    Object value;


    public FieldRadioButton(Context context, String text, String value, FieldView.Builder builder) {
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
