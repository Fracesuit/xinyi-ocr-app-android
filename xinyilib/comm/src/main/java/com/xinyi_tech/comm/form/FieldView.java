package com.xinyi_tech.comm.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.StringUtils2;

import butterknife.ButterKnife;

/**
 * Created by zhiren.zhang on 2018/3/16.
 */

public class FieldView extends FrameLayout {

    public static final int TYPE_EDITTEXT = 1;
    public static final int TYPE_TEXTVIEW = 2;
    public static final int TYPE_RADIOGROUP = 3;
    public static final int TYPE_SPINNER = 4;
    public static final int TYPE_HIDDENVIEW = 5;
    public static final int TYPE_MUTIVIEW = 6;
    public static final int TYPE_CHECKBOX = 7;

    Context context;

    Builder builder;
    private AppCompatTextView tv_label;
    private LinearLayout mLl_container;
    private LinearLayout mLl_field;

    public static Builder newBuilder() {
        return new Builder();
    }

    public FieldView(@NonNull Context context) {
        this(null, context);
    }

    public FieldView(Builder builder, Context context) {
        super(context, null);
        this.builder = builder;
        init(context, null);
    }


    public FieldView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        this.context = context;
        initAttributeSet(attrs);
        initView();
    }

    private void initAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldView);
            builder = new Builder()
                    .labelName(ta.getText(R.styleable.FieldView_comm_label_name))
                    .fieldName(ta.getString(R.styleable.FieldView_comm_field_name))
                    .dictKey(ta.getString(R.styleable.FieldView_comm_dict_key))
                    .dictDefaultName(ta.getString(R.styleable.FieldView_comm_dict_default_name))
                    .dictDefaultValue(ta.getString(R.styleable.FieldView_comm_dict_default_value))
                    .valueInitContent(ta.getString(R.styleable.FieldView_comm_init_content))
                    .warnMessager(ta.getString(R.styleable.FieldView_comm_warnMessager))
                    .mustInput(ta.getBoolean(R.styleable.FieldView_comm_must, false))
                    .lineHalfShow(ta.getBoolean(R.styleable.FieldView_comm_line_half_show, false))
                    .showMustInput(ta.getBoolean(R.styleable.FieldView_comm_showMustInput, true))
                    .valueViewType(ta.getInteger(R.styleable.FieldView_comm_contentType, -1))
                    .primaryIndex(ta.getInteger(R.styleable.FieldView_comm_field_primary_index, 0))
                    .textIcon(ta.getResourceId(R.styleable.FieldView_comm_textIcon, -1))
                    .fieldIndex(ta.getInteger(R.styleable.FieldView_comm_field_index, -1))
                    .edittextHint(ta.getString(R.styleable.FieldView_comm_edittext_hint))
                    .mustInputforMutiview(ta.getString(R.styleable.FieldView_comm_must_for_mutiview))
                    .edittextLine(ta.getInt(R.styleable.FieldView_comm_edittext_line, 1))
                    .fieldDivided(ta.getBoolean(R.styleable.FieldView_comm_fieldDivided, false))
                    .fieldEnable(ta.getBoolean(R.styleable.FieldView_comm_field_enable, true))
                    .forceIgnore(ta.getBoolean(R.styleable.FieldView_comm_forceIgnore, false))
                    .formDivided(ta.getBoolean(R.styleable.FieldView_comm_formDivided, true))
                    .formHorizontal(ta.getBoolean(R.styleable.FieldView_comm_formHorizontal, true))
                    .labelWithMustIcon(ta.getBoolean(R.styleable.FieldView_comm_labelWithMustIcon, false))
                    .labelTextSize(ta.getDimensionPixelSize(R.styleable.FieldView_comm_labelTextSize, SizeUtils.sp2px(14)))
                    .valueTextSize(ta.getDimensionPixelSize(R.styleable.FieldView_comm_valueTextSize, SizeUtils.sp2px(14)))
                    .labelWidth(ta.getDimensionPixelSize(R.styleable.FieldView_comm_labelWidth, -1))
                    .labelTextColor(ta.getResourceId(R.styleable.FieldView_comm_labelTextColor, R.color.comm_black))
                    .labelBgColor(ta.getResourceId(R.styleable.FieldView_comm_labelBgColor, android.R.color.transparent))
                    .dataBgColor(ta.getResourceId(R.styleable.FieldView_comm_dataBgColor, android.R.color.transparent))
                    .fieldViewBgColor(ta.getResourceId(R.styleable.FieldView_comm_fieldViewBgColor, R.color.comm_white))
                    .dividedColor(ta.getResourceId(R.styleable.FieldView_comm_dividedColor, R.color.comm_grey200))
                    .valueTextColor(ta.getResourceId(R.styleable.FieldView_comm_valueTextColor, R.color.comm_grey500))
                    .valueBgColor(ta.getResourceId(R.styleable.FieldView_comm_valueBgColor, android.R.color.transparent));
            ta.recycle();
        }
    }

    private void initView() {

        View fieldView = View.inflate(context, R.layout.comm_view_form_field, this);

        LinearLayout ll_root = ButterKnife.findById(fieldView, R.id.ll_root);
        mLl_field = ButterKnife.findById(fieldView, R.id.ll_field);

        mLl_container = ButterKnife.findById(fieldView, R.id.ll_container);
        View line_half = ButterKnife.findById(fieldView, R.id.line_half);
        ImageView img_icon = ButterKnife.findById(fieldView, R.id.img_icon);
        ImageView img_must = ButterKnife.findById(fieldView, R.id.img_must);
        View line_field = ButterKnife.findById(fieldView, R.id.line_field);
        View line_form = ButterKnife.findById(fieldView, R.id.line_form);


        //root背景
        mLl_field.setBackgroundResource(builder.fieldViewBgColor);
        mLl_container.setBackgroundResource(builder.valueBgColor);

        //label
        tv_label = new AppCompatTextView(context);
        if (builder.labelName != null) {
            if (builder.formHorizontal) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tv_label.setGravity(Gravity.CENTER);
                mLl_field.addView(tv_label, 0, layoutParams);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv_label.setGravity(Gravity.CENTER_VERTICAL);
                ll_root.addView(tv_label, 0, layoutParams);
            }

            tv_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.labelTextSize);
            tv_label.setText(builder.labelName);
            if (builder.labelWidth != -1) {
                tv_label.setWidth(builder.labelWidth);
            }
            tv_label.setTextColor(ContextCompat.getColor(getContext(), builder.labelTextColor));
            tv_label.setBackgroundResource(builder.labelBgColor);
        }


        //分割线
        line_field.setVisibility(builder.fieldDivided ? VISIBLE : GONE);
        line_form.setVisibility(builder.formDivided ? VISIBLE : GONE);
        line_half.setVisibility(builder.lineHalfShow ? VISIBLE : GONE);
        line_field.setBackgroundResource(builder.dividedColor);
        line_form.setBackgroundResource(builder.dividedColor);

        //icon
        if (builder.textIcon != -1) {
            img_icon.setVisibility(VISIBLE);
            img_icon.setImageResource(builder.textIcon);
        }

        //必须填写的标志
        if (builder.mustInput && builder.showMustInput) {//必须输入且要显示
            if (builder.labelWithMustIcon) {
                //放在前面
                if (tv_label != null)
                    FormInitUtil.modifyTextViewDrawable(tv_label, 2);
                img_must.setVisibility(GONE);
            } else {
                //放在后面
                img_must.setVisibility(VISIBLE);
                tv_label.setCompoundDrawables(null, null, null, null);
            }
        } else {
            img_must.setVisibility(GONE);
            tv_label.setCompoundDrawables(null, null, null, null);
        }
        //dataview
        initContentViewByType(null);
    }


    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.addView(child, params);
            return;
        }
        removeView(child);
        builder.dataView = child;
        initContentViewByType(params);
    }

    private void initContentViewByType(ViewGroup.LayoutParams params) {
        FrameLayout ll_data = ButterKnife.findById(this, R.id.ll_data);
        switch (builder.valueViewType) {
            case TYPE_EDITTEXT:
                builder.dataView = new FieldEditText(builder, context);
                break;
            case TYPE_TEXTVIEW:
                builder.dataView = new FieldTextView(builder, getContext());
                break;
            case TYPE_RADIOGROUP:
                builder.dataView = new FieldRadioGroup(builder, getContext());
                break;
            case TYPE_SPINNER:
                builder.dataView = new FieldSpinner(builder, getContext());
                break;
            case TYPE_CHECKBOX:
                builder.dataView = new FieldCheckBoxGroup(builder, getContext());
                break;
            case TYPE_HIDDENVIEW:
                setVisibility(GONE);
                builder.dataView = new HiddenFieldView(builder, getContext());
                break;
            default:
                initDataView(builder.dataView, false);
        }
        if (builder.dataView != null) {
            if (params == null) {
                params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            }
            ll_data.addView(builder.dataView, 0, params);
        }

    }

    private void initDataView(View view, boolean showMustIcon) {
        if (view != null) {
            if (view instanceof EditText) {
                FormInitUtil.initEditText((EditText) view, builder, showMustIcon);
            } else if (view instanceof TextView) {
                FormInitUtil.initTextView((TextView) view, builder, showMustIcon);
            } else if (view instanceof IFormField) {
                return;
            } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
                ViewGroup chlid = (ViewGroup) view;
                String[] fieldNames = fieldViewParamsCheck(getFieldName());
                String[] mustInputforMutiviews = fieldViewParamsCheck(getMustInputforMutiview(), isMustInput() ? "1" : "0");
                for (int i = 0, len = fieldNames.length; i < len; i++) {
                    if (StringUtils.isEmpty(fieldNames[i])) continue;
                    initDataView(chlid.getChildAt(i), ("1".equals(mustInputforMutiviews[i]) && FieldView.TYPE_MUTIVIEW == getValueViewType()) ? true : false);
                }
            }
        }
    }

    public String[] fieldViewParamsCheck(String params) {
        return fieldViewParamsCheck(params, null);
    }

    public String[] fieldViewParamsCheck(String params, String defaultParams) {
        if (FieldView.TYPE_MUTIVIEW == getValueViewType()) {
            ViewGroup childViews = (ViewGroup) builder.dataView;
            int childCount = childViews.getChildCount();
            if (StringUtils.isEmpty(params)) {
                String[] strings = new String[childCount];
                for (int i = 0; i < childCount; i++) {
                    strings[i] = defaultParams;
                }
                return strings;
            } else {
                String[] split = params.split(",");
                if (childCount != split.length) {
                    throw new RuntimeException(builder.labelName + "的类型为TYPE_MUTIVIEW,其设定的参数的长度不匹配,子view的个数和逗号不相等");
                }
                return split;
            }
        }
        return new String[]{defaultParams != null ? defaultParams : params};
    }


    private Object getChlidText(View view, int index) {
        if (view instanceof IFormField) {
            return ((IFormField) view).getValue();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
            ViewGroup chlid = (ViewGroup) view;
            return getChlidText(chlid.getChildAt(index), index);
        }
        return "";
    }


    public Object getValue() {
        return getValue(0);
    }

    public Object getValue(int index) {
        return getChlidText(builder.dataView, index);
    }

    private void setChlidText(View view, Object value, int index) {
        if (view instanceof IFormField) {
            ((IFormField) view).setVaule(value);
        } else if (view instanceof TextView) {
            value = StringUtils2.isEmpty(value) ? "" : value;
            ((TextView) view).setText(String.valueOf(value));
        } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
            ViewGroup chlid = (ViewGroup) view;
            setChlidText(chlid.getChildAt(index), value, index);
        }
    }

    public void setValue(Object value) {
        setValue(value, builder.primaryIndex);
    }

    public void setValue(Object value, int index) {
        setChlidText(builder.dataView, value, index);
    }

    public boolean isMustInput() {
        return builder.mustInput;
    }

    public String getWarnMessager() {
        return builder.warnMessager;
    }

    public String getMustInputforMutiview() {
        return builder.mustInputforMutiview;
    }

    public String getFieldName() {
        return builder.fieldName;
    }

    public String getDictKey() {
        return builder.dictKey;
    }

    public String getDictDefaultName() {
        return builder.dictDefaultName;
    }

    public String getDictDefaultValue() {
        return builder.dictDefaultValue;
    }


    public String getLabelName() {
        if (builder.labelName != null) {
            return builder.labelName.toString().replace(":", "").replace(" ", "").trim();
        }
        return null;
    }

    public View getDataView() {
        return builder.dataView;
    }

    public int getValueViewType() {
        return builder.valueViewType;
    }

    public void setLabelName(String labelName) {
        builder.labelName(labelName);
        boolean contains = tv_label.getText().toString().contains(":");
        tv_label.setText(labelName + (contains ? ":" : ""));
    }

    public void setFieldName(String fieldName) {
        builder.fieldName(fieldName);
    }

    public void setMustInput(boolean mustInput) {
        ImageView img_must = ButterKnife.findById(this, R.id.img_must);
        builder.mustInput = mustInput;
        if (builder.labelWithMustIcon) {
            if (mustInput) {
                FormInitUtil.modifyTextViewDrawable(tv_label, 2);
            } else {
                tv_label.setCompoundDrawables(null, null, null, null);
            }

        } else {
            img_must.setVisibility(mustInput && builder.mustInput ? VISIBLE : GONE);
        }

    }

    public void setValueBgColor(int valueBgColor) {
        builder.valueBgColor = valueBgColor;
        mLl_container.setBackgroundResource(builder.valueBgColor);
    }

    public void setLabelBgColor(int labelBgColor) {
        builder.labelBgColor = labelBgColor;
        tv_label.setBackgroundResource(builder.labelBgColor);
    }

    public void setFieldViewBgColor(int fieldViewBgColor) {
        builder.fieldViewBgColor = fieldViewBgColor;
        mLl_field.setBackgroundResource(builder.fieldViewBgColor);
    }

    public int getFieldIndex() {
        return builder.fieldIndex;
    }

    public void readOnly(int index) {
        setReadOnly(builder.dataView, index);
    }

    private void setReadOnly(View view, int index) {
        if (view instanceof IFormField) {
            view.setEnabled(false);
        } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
            ViewGroup chlid = (ViewGroup) view;
            setReadOnly(chlid.getChildAt(index), index);
        } else {
            view.setEnabled(false);
        }
    }

    public boolean isForceIngnore() {
        return builder.forceIgnore;
    }

    public static final class Builder {
        private int fieldViewBgColor = R.color.comm_white;
        private CharSequence labelName;
        private int labelTextColor = R.color.comm_black;
        private int labelTextSize = SizeUtils.sp2px(14);
        private int labelBgColor = android.R.color.transparent;
        private int dataBgColor = android.R.color.transparent;
        private int labelWidth = -1;
        private boolean labelWithMustIcon = false;
        private View dataView;
        private int valueViewType = -1;
        private int primaryIndex = 0;
        private int valueTextSize = SizeUtils.sp2px(14);
        private String warnMessager;
        private boolean mustInput = false;
        private boolean showMustInput = true;
        private boolean fieldDivided = true;
        private boolean forceIgnore = false;
        private boolean fieldEnable = true;
        private boolean formDivided = false;
        private boolean formHorizontal = true;
        private boolean lineHalfShow = false;
        private int dividedColor = R.color.comm_grey200;
        private int valueTextColor = R.color.comm_grey500;
        private int valueBgColor = android.R.color.transparent;
        private String fieldName;
        private String dictKey;
        private String dictDefaultName;
        private String dictDefaultValue;
        private String valueInitContent;
        private String mustInputforMutiview;
        private int textIcon = -1;
        private int fieldIndex = -1;
        private String edittextHint;
        private int edittextLine = -1;
        private int edittextTextlength;

        private Builder() {
        }

        public Builder fieldViewBgColor(int val) {
            fieldViewBgColor = val;
            return this;
        }

        public Builder mustInputforMutiview(String val) {
            mustInputforMutiview = val;
            return this;
        }

        public Builder labelName(CharSequence val) {
            labelName = val;
            return this;
        }

        public Builder labelTextColor(int val) {
            labelTextColor = val;
            return this;
        }

        public Builder labelTextSize(int val) {
            labelTextSize = val;
            return this;
        }

        public Builder labelBgColor(int val) {
            labelBgColor = val;
            return this;
        }

        public Builder dataBgColor(int val) {
            dataBgColor = val;
            return this;
        }

        public Builder labelWidth(int val) {
            labelWidth = val;
            return this;
        }

        public Builder labelWithMustIcon(boolean val) {
            labelWithMustIcon = val;
            return this;
        }

        public Builder lineHalfShow(boolean val) {
            lineHalfShow = val;
            return this;
        }

        public Builder dataView(View val) {
            dataView = val;
            return this;
        }

        public Builder valueViewType(int val) {
            valueViewType = val;
            return this;
        }

        public Builder primaryIndex(int val) {
            primaryIndex = val;
            return this;
        }

        public Builder valueTextSize(int val) {
            valueTextSize = val;
            return this;
        }

        public Builder warnMessager(String val) {
            warnMessager = val;
            return this;
        }

        public Builder valueInitContent(String val) {
            valueInitContent = val;
            return this;
        }

        public Builder mustInput(boolean val) {
            mustInput = val;
            return this;
        }

        public Builder showMustInput(boolean val) {
            showMustInput = val;
            return this;
        }

        public Builder fieldDivided(boolean val) {
            fieldDivided = val;
            return this;
        }

        public Builder fieldEnable(boolean val) {
            fieldEnable = val;
            return this;
        }

        public Builder forceIgnore(boolean val) {
            forceIgnore = val;
            return this;
        }

        public Builder formDivided(boolean val) {
            formDivided = val;
            return this;
        }

        public Builder dividedColor(int val) {
            dividedColor = val;
            return this;
        }

        public Builder valueTextColor(int val) {
            valueTextColor = val;
            return this;
        }

        public Builder valueBgColor(int val) {
            valueBgColor = val;
            return this;
        }

        public Builder fieldName(String val) {
            fieldName = val;
            return this;
        }

        public Builder dictKey(String val) {
            dictKey = val;
            return this;
        }

        public Builder dictDefaultName(String val) {
            dictDefaultName = val;
            return this;
        }

        public Builder dictDefaultValue(String val) {
            dictDefaultValue = val;
            return this;
        }


        public Builder textIcon(int val) {
            textIcon = val;
            return this;
        }

        public Builder fieldIndex(int val) {
            fieldIndex = val;
            return this;
        }

        public Builder edittextHint(String val) {
            edittextHint = val;
            return this;
        }

        public Builder edittextLine(int val) {
            edittextLine = val;
            return this;
        }

        public Builder edittextTextlength(int val) {
            edittextTextlength = val;
            return this;
        }

        public Builder formHorizontal(boolean val) {
            formHorizontal = val;
            return this;
        }

        public FieldView build(Context context) {
            return new FieldView(this, context);
        }

        public String getValueInitContent() {
            return valueInitContent;
        }


        public int getValueTextSize() {
            return valueTextSize;
        }

        public int getValueTextColor() {
            return valueTextColor;
        }

        public String getEdittextHint() {
            return edittextHint;
        }

        public int getEdittextLine() {
            return edittextLine;
        }

        public int getDataBgColor() {
            return dataBgColor;
        }

        public boolean isFieldEnable() {
            return fieldEnable;
        }

        public View getDataView() {
            return dataView;
        }
    }


}
