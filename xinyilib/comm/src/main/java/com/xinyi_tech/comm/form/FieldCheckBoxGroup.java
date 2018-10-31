package com.xinyi_tech.comm.form;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.xinyi_tech.comm.util.StringUtils2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/6/20.
 */

public class FieldCheckBoxGroup extends FlexboxLayout implements IFormField {

    List<DictField> datas = new ArrayList<>();
    String defaultCheckValue = null;//1,2,3
    FieldView.Builder builder;

    public FieldCheckBoxGroup(FieldView.Builder builder, Context context) {
        super(context);
        init(builder);
    }

    private void init(FieldView.Builder builder) {
        setFlexWrap(FlexWrap.WRAP);
        setJustifyContent(JustifyContent.FLEX_START);
        this.builder = builder;
        if (builder != null && !StringUtils2.isEmpty(builder.getValueInitContent())) {//  yi_1,er_2,san_3,check_1&2&3
            for (String name_value : builder.getValueInitContent().split(",")) {
                final String[] split = name_value.split("_");
                if ("check".equals(split[0])) {
                    defaultCheckValue = split[1].replace("&", ",");
                    continue;
                }
                datas.add(new DictField(split[0], split[1]));
            }
            inflateView();
        }

        setEnabled(builder.isFieldEnable());
    }

    private void inflateView() {
        removeAllViews();
        final FieldCheckBoxGroup.LayoutParams layoutParams = new FieldCheckBoxGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (DictField dict : datas) {
            final FieldCheckBox fieldCheckBox = new FieldCheckBox(getContext(), dict.getName(), dict.getValue(), builder);
            addView(fieldCheckBox, layoutParams);
        }
        //设置默认值
        if (defaultCheckValue != null) {
            setVaule(defaultCheckValue);
        }
    }

    @Override
    public String getValue() {
        ArrayList<String> objects = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof FieldCheckBox) {
                FieldCheckBox fieldCheckBox = (FieldCheckBox) view;
                if (fieldCheckBox.isChecked()) {
                    sb.append(fieldCheckBox.getValue() + ",");
                }
            }
        }
        int length = sb.toString().length();
        if (length == 0) {
            return null;
        }
        return sb.toString().substring(0, length - 1);
    }

    @Override
    public void setVaule(Object value) {
        String setValue = value == null ? defaultCheckValue == null ? "" : defaultCheckValue : (String) value;
        String[] values = setValue.replace(" ", "").split(",");
        for (String v : values) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof FieldCheckBox) {
                    String selectValue = (String) ((FieldCheckBox) view).getValue();
                    if (selectValue.equals(v)) {
                        ((FieldCheckBox) view).setChecked(true);
                        break;
                    }
                }
            }

        }
    }

    public void setData(List<DictField> datas, String defaultCheckValue) {
        this.datas = datas;
        this.defaultCheckValue = defaultCheckValue;
        inflateView();
    }

    public void setData(List<DictField> datas) {
        setData(datas, "");
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.setEnabled(enabled);
        }
    }
}
