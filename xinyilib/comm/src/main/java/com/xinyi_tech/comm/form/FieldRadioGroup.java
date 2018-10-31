package com.xinyi_tech.comm.form;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.xinyi_tech.comm.util.StringUtils2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fracesuit on 2017/8/8.
 */

public class FieldRadioGroup extends RadioGroup implements IFormField {

    List<DictField> datas = new ArrayList<>();
    String defaultCheckValue = null;
    FieldView.Builder builder;
    public FieldRadioGroup(FieldView.Builder builder, Context context) {
        super(context);
        init(builder);
    }

    private void init(FieldView.Builder builder) {
        this.builder = builder;
        if (builder != null && !StringUtils2.isEmpty(builder.getValueInitContent())) {

            for (String name_value : builder.getValueInitContent().split(",")) {
                final String[] split = name_value.split("_");
                if ("check".equals(split[0])) {
                    defaultCheckValue = split[1];
                    continue;
                }
                datas.add(new DictField(split[0], split[1]));
            }
            inflateView();
        }

        setEnabled(builder.isFieldEnable());
    }


    private void inflateView() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        removeAllViews();
        final LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        for (DictField dict : datas) {
            final FieldRadioButton fieldRadioButton = new FieldRadioButton(getContext(), dict.getName(), dict.getValue(),builder);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                fieldRadioButton.setId(View.generateViewId());
            }
            addView(fieldRadioButton, layoutParams);
        }

        //设置默认值
        if (defaultCheckValue != null) {
            setVaule(defaultCheckValue);
        }


    }

    @Override
    public Object getValue() {
        View view = findViewById(getCheckedRadioButtonId());
        if (view != null && view instanceof FieldRadioButton) {
            return ((FieldRadioButton) view).getValue();
        }
        return null;
    }

    @Override
    public void setVaule(Object value) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof FieldRadioButton) {
                String selectValue = (String) ((FieldRadioButton) view).getValue();
                String setValue = value == null ? defaultCheckValue == null ? "" : defaultCheckValue : (String) value;
                if (selectValue.equals(setValue)) {
                    ((FieldRadioButton) view).setChecked(true);
                    break;
                }

            }
        }
    }

    public String getName() {
        final String value = (String) getValue();
        for (DictField dictField : datas) {
            if (dictField.getValue().equals(value)) {
                return dictField.getName();
            }
        }
        return null;
    }


    public void setData(List<DictField> datas, String defaultCheckValue) {
        this.datas = datas;
        this.defaultCheckValue = defaultCheckValue;
        inflateView();
    }

    public void setData(List<DictField> datas) {
        setData(datas, "");
    }

    public void setDataByDefaultName(List<DictField> datas, String defaultCheckName) {
        String defaultValue = null;
        for (DictField dictField : datas) {
            if (dictField.getName().equals(defaultCheckName)) {
                defaultValue = dictField.getValue();
                break;
            }
        }
        setData(datas, defaultValue);
    }

    public interface OnCheckChangeListener {
        void onItemSelected(DictField dictField);
    }

    public void setOnCheckChangeListener(final OnCheckChangeListener onCheckChangeListener) {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final String value = (String) getValue();
                for (DictField dictField : datas) {
                    if (dictField.getValue().equals(value)) {
                        onCheckChangeListener.onItemSelected(dictField);
                    }
                }
            }
        });
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
