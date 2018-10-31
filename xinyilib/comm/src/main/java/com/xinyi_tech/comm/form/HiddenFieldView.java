package com.xinyi_tech.comm.form;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;

import com.xinyi_tech.comm.util.StringUtils2;


/**
 * Created by Fracesuit on 2017/8/8.
 */

public class HiddenFieldView extends AppCompatTextView implements IFormField, IHiddenField {
    FieldView.Builder builder;

    public HiddenFieldView(FieldView.Builder builder, Context context) {
        super(context);
        this.builder = builder;
        init();
    }

    private void init() {
        if (builder != null) {
            setText(builder.getValueInitContent());
        }
        hidden();
    }


    @Override
    public Object getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(Object value) {
        if (builder.isFieldEnable()) {
            value = StringUtils2.isEmpty(value) ? "" : value;
            setText(String.valueOf(value));
        }
    }


    @Override
    public void hidden() {
        setVisibility(GONE);
    }
}
