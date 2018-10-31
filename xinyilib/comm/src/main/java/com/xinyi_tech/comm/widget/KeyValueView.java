package com.xinyi_tech.comm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.ResUtils;

/**
 * Created by zhiren.zhang on 2017/11/29.
 */

public class KeyValueView extends LinearLayout {
    public KeyValueView(Context context) {
        this(context, null);
    }

    public KeyValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TextView keyTextView = new TextView(getContext());
        TextView valueTextView = new TextView(getContext());
        keyTextView.setTextColor(ResUtils.getColor(R.color.comm_black));
        valueTextView.setTextColor(ResUtils.getColor(R.color.comm_grey));
        keyTextView.setTextSize(14);
        valueTextView.setTextSize(14);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.KeyValueView);
            String keyText = typedArray.getString(R.styleable.KeyValueView_comm_key_text);
            String valueText = typedArray.getString(R.styleable.KeyValueView_comm_value_text);
            float keyTextSize = typedArray.getDimension(R.styleable.KeyValueView_comm_key_size, SizeUtils.sp2px(14));
            float valueTextSize = typedArray.getDimension(R.styleable.KeyValueView_comm_value_size, SizeUtils.sp2px(14));
            int key_color = typedArray.getResourceId(R.styleable.KeyValueView_comm_key_color, R.color.comm_black);
            int value_color = typedArray.getResourceId(R.styleable.KeyValueView_comm_value_color, R.color.comm_grey);
            int kv_gravity = typedArray.getInt(R.styleable.KeyValueView_comm_kv_gravity, 1);

            typedArray.recycle();
            keyTextView.setText(keyText);
            valueTextView.setText(valueText);
            keyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,keyTextSize);
            valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,valueTextSize);
            keyTextView.setTextColor(ResUtils.getColor(key_color));
            valueTextView.setTextColor(ResUtils.getColor(value_color));
            valueTextView.setGravity(kv_gravity == 1 ? Gravity.LEFT : kv_gravity == 2 ? Gravity.CENTER : Gravity.RIGHT);
        }

        LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams valueLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        valueLayoutParams.leftMargin = SizeUtils.dp2px(10);
        addView(keyTextView, keyLayoutParams);
        addView(valueTextView, valueLayoutParams);
    }

    public void setKeyText(String text) {
        TextView childAt = (TextView) getChildAt(0);
        childAt.setText(text);
    }

    public void setValueText(String text) {
        TextView childAt = (TextView) getChildAt(1);
        childAt.setText(text);
    }
}
