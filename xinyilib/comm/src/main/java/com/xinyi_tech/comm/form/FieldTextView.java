package com.xinyi_tech.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xinyi_tech.comm.util.StringUtils2;


/**
 * Created by Fracesuit on 2017/8/8.
 */

public class FieldTextView extends AppCompatTextView implements IFormField {
    public FieldTextView(FieldView.Builder builder, Context context) {
        super(context);
        FormInitUtil.initTextView(this, builder);
    }

    public FieldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Object getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(Object value) {
        value = StringUtils2.isEmpty(value) ? "" : value;
        setText(String.valueOf(value));
    }
}
