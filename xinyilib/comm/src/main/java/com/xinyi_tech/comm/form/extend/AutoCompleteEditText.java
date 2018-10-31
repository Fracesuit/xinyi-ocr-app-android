package com.xinyi_tech.comm.form.extend;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.xinyi_tech.comm.CommCallBackListener;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.form.DictField;
import com.xinyi_tech.comm.form.IDictField;
import com.xinyi_tech.comm.form.IFormField;
import com.xinyi_tech.comm.util.ActivityUtils2;
import com.xinyi_tech.comm.util.StringUtils2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhiren.zhang on 2018/6/22.
 */

public class AutoCompleteEditText extends AppCompatEditText implements IFormField, IDictField {
    private AutoCompletePopopWindow popopWindow;
    CommCallBackListener<String> commCallBackListener;
    CommCallBackListener<DictField> onItemClickListener;
    boolean isSelected = false;
    List<DictField> datas;
    boolean dictMode;
    boolean supportFirstChangeCallBack = true;//第一次设置值得时候，是否支持相应onitemchange事件

    public AutoCompleteEditText(Context context, boolean dismissWhenTouchOutside, boolean dictMode) {
        super(context);
        this.dictMode = dictMode;
        init(dismissWhenTouchOutside);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoCompleteEditText);
        boolean dismissWhenTouchOutside = ta.getBoolean(R.styleable.AutoCompleteEditText_dismissWhenTouchOutside, true);
        dictMode = ta.getBoolean(R.styleable.AutoCompleteEditText_dictMode, false);
        init(dismissWhenTouchOutside);
    }

    public void setCommCallBackListener(CommCallBackListener<String> commCallBackListener) {
        this.commCallBackListener = commCallBackListener;
    }

    public void setCommCallBackListener(boolean supportFirstChangeCallBack, CommCallBackListener<String> commCallBackListener) {
        this.supportFirstChangeCallBack = supportFirstChangeCallBack;
        this.commCallBackListener = commCallBackListener;
    }


    public void setDataByStr(List<String> datas, boolean isShow) {
        ArrayList<DictField> dictFields = new ArrayList<>();
        if (datas != null) {
            for (String name : datas) {
                DictField dictField = new DictField(name, name);
                dictFields.add(dictField);
            }
        }
        setData(dictFields, isShow);
        ;


    }

    public void setDataByStr(List<String> datas) {
        setDataByStr(datas, false);
    }

    @Override
    public void setData(List<DictField> datas) {
        setData(datas, false);
    }

    public void setData(List<DictField> datas, boolean isShow) {
        this.datas = datas;
        popopWindow.setData(datas);
        if (datas != null && datas.size() > 0) {
            if (isShow && !popopWindow.isShowing()) {
                popopWindow.showPopupWindow(this);
            }
        } else {
            if (popopWindow.isShowing()) {
                popopWindow.dismiss();
            }
        }
    }

    public void init(boolean dismissWhenTouchOutside) {
        //状态
        popopWindow = new AutoCompletePopopWindow(ActivityUtils2.getActivityFromView(this), dismissWhenTouchOutside);
        popopWindow.setCommCallBackListener(new CommCallBackListener<DictField>() {
            @Override
            public void callBack(DictField dictField) {
                popopWindow.dismiss();
                isSelected = true;
                setVaule(dictField);
                setSelection(dictField.getName().length());
                if (onItemClickListener != null) {
                    onItemClickListener.callBack(dictField);
                }
            }
        });
        RxTextView.textChanges(this)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (commCallBackListener != null) {
                            if (!StringUtils.isEmpty(charSequence) && !isSelected) {
                                if (supportFirstChangeCallBack) {
                                    commCallBackListener.callBack(charSequence.toString());
                                } else {
                                    supportFirstChangeCallBack = true;
                                }

                            } else {
                                isSelected = false;
                            }
                        } else {
                            if (!isSelected) {
                                popopWindow.filter(charSequence.toString());
                                if (popopWindow.getData() != null && popopWindow.getData().size() > 0) {
                                    if (!popopWindow.isShowing() && AutoCompleteEditText.this.hasFocus()) {
                                        popopWindow.showPopupWindow(AutoCompleteEditText.this);
                                    }
                                } else {
                                    if (popopWindow.isShowing()) {
                                        popopWindow.dismiss();
                                    }
                                }
                            } else {
                                isSelected = false;
                            }

                        }
                    }
                });

        RxView.focusChanges(this)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            if (datas != null && datas.size() > 0) {
                                if (!popopWindow.isShowing()) {
                                    popopWindow.showPopupWindow(AutoCompleteEditText.this);
                                }
                            }
                        }
                    }
                });
    }

    Object value;

    @Override
    public Object getValue() {
        if (dictMode) {
            return value;
        } else {
            return getText().toString();
        }

    }

    @Override
    public void setVaule(Object value) {
        if (dictMode) {
            if (value instanceof DictField) {
                DictField dictField = (DictField) value;
                this.value = StringUtils2.isEmpty(dictField.getValue()) ? "" : dictField.getValue();
                setText(dictField.getName());
            } else if (value instanceof String) {
                String str = (String) value;
                str = StringUtils2.isEmpty(str) ? "" : str;
                if (datas != null && datas.size() > 0) {
                    for (DictField dictField : datas) {
                        if (str.equals(dictField.getValue())) {
                            this.value = StringUtils2.isEmpty(dictField.getValue()) ? "" : value;
                            setText(dictField.getName());
                            break;
                        }
                    }
                } else {
                    this.value = StringUtils2.isEmpty(str) ? "" : value;
                    setText(str);
                }
            }
        } else {
            value = StringUtils2.isEmpty(value) ? "" : value;
            setText(String.valueOf(value));
        }
    }

    public void setOnItemClickListener(CommCallBackListener<DictField> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String getValueByName(String name) {
        if (datas != null) {
            for (DictField dictField : datas) {
                if (dictField.getName().equals(name)) {
                    return dictField.getValue();
                }
            }
        }

        return null;

    }

    public DictField getSelectDictField() {
        if (datas != null) {
            for (DictField dictField : datas) {
                if (dictField.getValue().equals(getValue())) {
                    return dictField;
                }
            }
        }
        return null;
    }
}
