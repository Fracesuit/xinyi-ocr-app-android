package com.xinyi_tech.comm.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.StringUtils2;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fracesuit on 2017/8/17.
 */

public class FieldSpinner extends AppCompatSpinner implements IFormField {

    List<DictField> datas = new ArrayList<>();
    FieldView.Builder builder;
    String defautSelectValue = null;
    boolean defaultIndexFirst = true;
    boolean supportFirstChangeCallBack = true;//第一次设置值得时候，是否支持相应onitemchange事件

    public FieldSpinner(FieldView.Builder builder, Context context) {
        this(context, null);
        init(builder);
    }

    public FieldSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldSpinner);
        builder = FieldView.newBuilder()
                .valueTextSize(ta.getDimensionPixelSize(R.styleable.FieldSpinner_android_textSize, SizeUtils.sp2px(14)))
                .valueTextColor(ta.getResourceId(R.styleable.FieldSpinner_android_textColor, R.color.comm_grey500));
        ta.recycle();
        init(builder);
    }

    private void init(FieldView.Builder builder) {
        this.builder = builder;
        if (builder != null && !StringUtils2.isEmpty(builder.getValueInitContent())) {

            if (builder.getValueInitContent() != null) {
                for (String name_value : builder.getValueInitContent().split(",")) {
                    final String[] split = name_value.split("_");
                    if ("check".equals(split[0])) {
                        defautSelectValue = split[1];
                        continue;
                    }
                    datas.add(new DictField(split[0], split[1]));
                }

                attachDataSource();
                setVaule(defautSelectValue);
            }
        }
        setEnabled(builder.isFieldEnable());
        setOnSpinnerItemSelectListener(null);
    }

    @Override
    public Object getValue() {
        final int selectedItemPosition = getSelectedItemPosition();
        if (selectedItemPosition > -1) {
            return datas.get(selectedItemPosition).getValue();
        } else {
            return null;
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

    @Override
    public void setVaule(@NonNull Object value) {
        String setValue = value == null ? defautSelectValue == null ? "" : defautSelectValue : (String) value;
        if (StringUtils2.isEmpty(setValue)) setSelection(0, true);
        int i = 0;
        for (DictField data : datas) {
            if (setValue != null && setValue.equals(data.getValue())) {
                setSelection(i, true);
                return;
            }
            i++;
        }

        //如果找不到，就设置选择第一个或者最后一个
        if (datas != null && datas.size() > 0) {
            setSelection(defaultIndexFirst ? 0 : datas.size() - 1, true);
        }

    }

    public void setDefaultIndexFirstOrLast(boolean first) {
        this.defaultIndexFirst = first;
    }


    public void setData(List<DictField> datas, String defautSelectValue) {
        this.datas = datas;
        attachDataSource();
        setVaule(defautSelectValue);
    }

    public void setData(List<DictField> datas) {
        setData(datas, null);
    }


    public void setDataByDefaultName(List<DictField> datas, String defaultCheckName) {
        String defaultCheckValue = null;
        for (DictField dictField : datas) {
            if (dictField.getName().equals(defaultCheckName)) {
                defaultCheckValue = dictField.getValue();
                break;
            }
        }

        setData(datas, defaultCheckValue);
    }

    private void attachDataSource() {
        ArrayAdapter<DictField> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, datas);
        adapter.setDropDownViewResource(R.layout.comm_item_auto_complete_edittext);
        setAdapter(adapter);

    }

    public interface OnSpinnerItemSelectListener {
        void onItemSelected(DictField dictField, int position);
    }

    public void setOnSpinnerItemSelectListener(final OnSpinnerItemSelectListener onSpinnerItemSelectListener) {
        setOnSpinnerItemSelectListener(true, onSpinnerItemSelectListener);
    }

    public void setOnSpinnerItemSelectListener(boolean supportFirstChangeCallBack, final OnSpinnerItemSelectListener onSpinnerItemSelectListener) {
        this.supportFirstChangeCallBack = supportFirstChangeCallBack;
        final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (builder != null) {
                    builder.valueInitContent(null);
                }

                FormInitUtil.initTextView(tv, builder);

                if (onSpinnerItemSelectListener != null && FieldSpinner.this.supportFirstChangeCallBack) {
                    DictField dictField = datas.get(position);
                    onSpinnerItemSelectListener.onItemSelected(dictField, position);
                } else {
                    FieldSpinner.this.supportFirstChangeCallBack = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        super.setOnItemSelectedListener(onItemSelectedListener);
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
