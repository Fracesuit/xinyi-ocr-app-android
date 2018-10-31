package com.xinyi_tech.comm.form;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.util.StringUtils2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_ALL;
import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_ALL_WITH_FORCEINGNORE;
import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE;
import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN;
import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN_WITH_FORCEINGNORE;
import static com.xinyi_tech.comm.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_WITH_FORCEINGNORE;

public class FormLayout extends LinearLayout {


    @IntDef({
            FIELD_TYPE_ALL,
            FIELD_TYPE_VISIBLE,
            FIELD_TYPE_VISIBLE_HIDDLEN,
            FIELD_TYPE_ALL_WITH_FORCEINGNORE,
            FIELD_TYPE_VISIBLE_WITH_FORCEINGNORE,
            FIELD_TYPE_VISIBLE_HIDDLEN_WITH_FORCEINGNORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionFieldType {
        int FIELD_TYPE_ALL = 0;//所有的view
        int FIELD_TYPE_VISIBLE = 1;//仅仅可见的view
        int FIELD_TYPE_VISIBLE_HIDDLEN = 2;//仅仅可见和隐藏的view

        int FIELD_TYPE_ALL_WITH_FORCEINGNORE = 10;//所有的view
        int FIELD_TYPE_VISIBLE_WITH_FORCEINGNORE = 11;//仅仅可见的view
        int FIELD_TYPE_VISIBLE_HIDDLEN_WITH_FORCEINGNORE = 12;//仅仅可见和隐藏的view
    }

    public void addFieldView(FieldView.Builder builder) {
        addView(builder.build(getContext()));
    }

    List<Integer> filedIndexs = new ArrayList<>();

    private int getIndexByfiledIndex(Integer filedIndex) {
        filedIndexs.add(filedIndex);
        Collections.sort(filedIndexs, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (int i = 0, len = filedIndexs.size(); i < len; i++) {
            Integer integer = filedIndexs.get(i);
            if (integer == filedIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        //如果是-1  序列之就是childCount;
        int childCount = getChildCount();
        int fieldIndex = childCount;
        if (child instanceof FieldView) {
            FieldView fieldView = (FieldView) child;
            fieldIndex = fieldView.getFieldIndex();
            if (fieldIndex == -1) {
                fieldIndex = childCount;
                fieldView.builder.fieldIndex(childCount);
            }
        }
        index = getIndexByfiledIndex(fieldIndex);
        super.addView(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }


    public FormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

    }

    /**
     * visibleType  为true 说明只获取可见的  为false  获取所有的
     *
     * @return
     */
    public List<FieldView> getFieldViews(@FormLayout.ActionFieldType int visibleType) {
        return getFieldViews(this, visibleType);
    }

    public List<FieldView> getFieldViews(ViewGroup parentView, @FormLayout.ActionFieldType int visibleType) {
        List<FieldView> formFields = new ArrayList<>();
        getFieldViewByStep(parentView, visibleType, formFields);
        return formFields;
    }

    private void getFieldViewByStep(ViewGroup parentView, @ActionFieldType int visibleType, List<FieldView> formFields) {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View view = parentView.getChildAt(i);
            if (view instanceof FormLayout) {
                continue;
            }
            if (view instanceof FieldView) {
                FieldView fieldView = (FieldView) view;

                int tempVisibleType = visibleType;

                //强制忽略
                if (tempVisibleType >= 10) {
                    if (fieldView.isForceIngnore()) {
                        continue;
                    } else {
                        tempVisibleType -= 10;
                    }
                }

                if (StringUtils.isEmpty(fieldView.getFieldName())) {
                    continue;
                }

                if (tempVisibleType == FIELD_TYPE_ALL) {
                    formFields.add(((FieldView) view));//所有的
                } else if (tempVisibleType == FIELD_TYPE_VISIBLE && fieldView.getVisibility() == View.VISIBLE) {
                    formFields.add(((FieldView) view));//所有可见的
                } else if (tempVisibleType == FIELD_TYPE_VISIBLE_HIDDLEN && (fieldView.getVisibility() == View.VISIBLE || fieldView.getDataView() instanceof IHiddenField)) {
                    formFields.add(((FieldView) view));//所有可见的+实现IHiddenField的
                }
            } else if (view instanceof ViewGroup) {
                if (view.getVisibility() == GONE && visibleType != FIELD_TYPE_ALL) {
                    continue;
                }
                getFieldViewByStep((ViewGroup) view, visibleType, formFields);
            }
        }
    }

    public FieldView getFieldViewByName(@NonNull String name, @FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (name.equalsIgnoreCase(fieldView.getFieldName())) {
                return fieldView;
            }
        }
        return null;
    }


    /**
     * 检查表单样式
     */
    public boolean checkForm(@FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
            for (int i = 0, len = fieldNames.length; i < len; i++) {
                if (StringUtils.isEmpty(fieldNames[i])) continue;
                String[] mWarnMessagers = fieldView.fieldViewParamsCheck(fieldView.getWarnMessager());
                String[] getMustInputforMutiviews = fieldView.fieldViewParamsCheck(fieldView.getMustInputforMutiview(), fieldView.isMustInput() ? "1" : "0");
                if ("1".equals(getMustInputforMutiviews[i]) && StringUtils2.isEmpty(fieldView.getValue(i))) {
                    showEmptyMessager(fieldView, mWarnMessagers[i]);
                    return false;
                }
            }
        }
        return true;
    }

    private void showEmptyMessager(FieldView fieldView, String warnMessager) {
        final String labelName = fieldView.getLabelName();
        String warn = null;
        if (!StringUtils.isEmpty(warnMessager)) {
            if (warnMessager.contains("%s")) {
                String[] split = warnMessager.split(",");
                if (split.length == 1) {
                    warn = String.format(warnMessager, labelName);
                } else {
                    View dataView = fieldView.getDataView();
                    if (dataView instanceof EditText) {
                        warn = String.format(split[0] + split[2], labelName);
                    } else {
                        warn = String.format(split[1] + split[2], labelName);
                    }
                }

            } else {
                warn = warnMessager;
            }
        } else if (!StringUtils.isEmpty(labelName)) {
            warn = labelName + "不得为空";
        } else {
            warn = "请确认表单,有必填项没有填写完成";
        }
        Toasty.warning(getContext(), warn, Toast.LENGTH_SHORT).show();
    }

    private Field getSuperField(Class clazz, String fieldName) {
        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return getSuperField(clazz.getSuperclass(), fieldName);
        }
        if (declaredField != null) {
            declaredField.setAccessible(true);
        }
        return declaredField;
    }

    public void bindData(Object datas, @FormLayout.ActionFieldType int visibleType) {
        if (datas == null) return;
        for (FieldView fieldView : getFieldViews(visibleType)) {
            String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
            for (int i = 0, len = fieldNames.length; i < len; i++) {
                if (StringUtils.isEmpty(fieldNames[i])) continue;
                Object value = null;
                try {
                    value = ReflectUtils.reflect(datas).field(fieldNames[i]).get();
                } catch (ReflectUtils.ReflectException e) {
                    e.printStackTrace();
                }
                value = StringUtils2.isEmpty(value) ? null : value;
                fieldView.setValue(value, i);
            }
        }
    }

    public void bindData(Map<String, Object> params, @FormLayout.ActionFieldType int visibleType) {
        if (params == null || params.size() == 0) return;
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
                for (int i = 0, len = fieldNames.length; i < len; i++) {
                    if (StringUtils.isEmpty(fieldNames[i])) continue;
                    Object value = params.get(fieldNames[i]);
                    value = StringUtils2.isEmpty(value) ? null : value;
                    fieldView.setValue(value, i);
                }
            } catch (Exception e) {
                XinYiLog.e(e.getMessage());
            }
        }
    }


    public Map<String, Object> getParams(@FormLayout.ActionFieldType int visibleType) {
        return getParamsByPrefix(this, "", visibleType);
    }

    public Map<String, Object> getParams(ViewGroup parentView, @FormLayout.ActionFieldType int visibleType) {
        return getParamsByPrefix(parentView, "", visibleType);
    }

    public Map<String, Object> getParamsByPrefix(String prefix, @FormLayout.ActionFieldType int visibleType) {
        return getParamsByPrefix(this, prefix, visibleType);
    }

    public Map<String, Object> getParamsByPrefix(ViewGroup parentView, String prefix, @FormLayout.ActionFieldType int visibleType) {
        Map<String, Object> map = new HashMap<>();
        for (FieldView fieldView : getFieldViews(parentView, visibleType)) {
            String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
            for (int i = 0, len = fieldNames.length; i < len; i++) {
                if (StringUtils.isEmpty(fieldNames[i])) continue;
                Object value = fieldView.getValue(i);
                if (StringUtils2.isEmpty(value)) continue;
                map.put(prefix + fieldNames[i], value);
            }
        }
        return map;
    }


    public <T> T getParamsByPrefix(Class<T> clazz, @FormLayout.ActionFieldType int visibleType, String prefix) {
        return getParamsByPrefix(this, clazz, visibleType, prefix);
    }


    public <T> T getParams(Class<T> clazz, @FormLayout.ActionFieldType int visibleType) {
        return getParamsByPrefix(clazz, visibleType, "");
    }

    public <T> T getParams(ViewGroup parentView, Class<T> clazz, @FormLayout.ActionFieldType int visibleType) {
        return getParamsByPrefix(parentView, clazz, visibleType, "");
    }

    public <T> T getParamsByPrefix(ViewGroup parentView, Class<T> clazz, @FormLayout.ActionFieldType int visibleType, String prefix) {
        Map<String, Object> map = getParamsByPrefix(parentView, prefix, visibleType);
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toJavaObject(clazz);
    }

    public List<String> getDictKeys() {
        final ArrayList<String> dictKeys = new ArrayList<>();
        for (FieldView fieldView : getFieldViews(ActionFieldType.FIELD_TYPE_ALL)) {
            final String dictKey = fieldView.getDictKey();
            if (!StringUtils.isEmpty(dictKey)) {
                final View dataView = fieldView.getDataView();
                if (dataView instanceof FieldRadioGroup || dataView instanceof FieldSpinner || dataView instanceof FieldCheckBoxGroup || dataView instanceof IDictField) {
                    dictKeys.add(dictKey);
                }
            }

        }
        return dictKeys;
    }

    public void setupDictData(Map<String, List<DictField>> dictMap) {
        for (FieldView fieldView : getFieldViews(ActionFieldType.FIELD_TYPE_ALL)) {
            final String dictKey = fieldView.getDictKey();
            if (!StringUtils.isEmpty(dictKey)) {
                final List<DictField> dictFields = dictMap.get(dictKey);
                if (dictFields != null && dictFields.size() > 0) {
                    final View dataView = fieldView.getDataView();
                    final String dictDefaultName = fieldView.getDictDefaultName();
                    final String dictDefaultValue = fieldView.getDictDefaultValue();
                    if (dataView instanceof FieldRadioGroup) {
                        FieldRadioGroup v = (FieldRadioGroup) dataView;
                        if (!StringUtils.isEmpty(dictDefaultName)) {
                            v.setDataByDefaultName(dictFields, dictDefaultName);
                        } else {
                            v.setData(dictFields, dictDefaultValue);
                        }
                    } else if (dataView instanceof FieldSpinner) {
                        FieldSpinner v = (FieldSpinner) dataView;
                        if (!StringUtils.isEmpty(dictDefaultName)) {
                            v.setDataByDefaultName(dictFields, dictDefaultName);
                        } else {
                            v.setData(dictFields, dictDefaultValue);
                        }
                    } else if (dataView instanceof FieldCheckBoxGroup) {
                        FieldCheckBoxGroup v = (FieldCheckBoxGroup) dataView;
                        v.setData(dictFields, dictDefaultValue);
                    } else if (dataView instanceof IDictField) {
                        IDictField v = (IDictField) dataView;
                        v.setData(dictFields);
                    }
                }
            }

        }
    }

    public void readOnly(@FormLayout.ActionFieldType int visibleType)
    {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
                for (int i = 0, len = fieldNames.length; i < len; i++) {
                    if (StringUtils.isEmpty(fieldNames[i])) continue;
                    fieldView.readOnly( i);
                }
            } catch (Exception e) {
                XinYiLog.e(e.getMessage());
            }
        }
    }

    public void reset(@FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                String[] fieldNames = fieldView.fieldViewParamsCheck(fieldView.getFieldName());
                for (int i = 0, len = fieldNames.length; i < len; i++) {
                    if (StringUtils.isEmpty(fieldNames[i])) continue;
                    fieldView.setValue(null, i);
                }
            } catch (Exception e) {
                XinYiLog.e(e.getMessage());
            }
        }
    }

}
