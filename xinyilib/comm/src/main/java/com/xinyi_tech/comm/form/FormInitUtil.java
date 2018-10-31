package com.xinyi_tech.comm.form;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.comm.util.StringUtils2;

/**
 * Created by zhiren.zhang on 2017/11/30.
 */

public class FormInitUtil {

    public static void initTextView(TextView textView, FieldView.Builder builder) {
        initTextView(textView, builder, false);
    }

    public static void initTextView(TextView textView, FieldView.Builder builder, boolean showMustIcon) {
        if (builder != null) {
            if (!StringUtils2.isEmpty(builder.getValueInitContent())) {
                textView.setText(builder.getValueInitContent());
            }
            if (!StringUtils2.isEmpty(builder.getEdittextHint())) {
                textView.setHint(builder.getEdittextHint());
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.getValueTextSize());
            textView.setTextColor(ResUtils.getColor(builder.getValueTextColor()));

            if (showMustIcon) {
                modifyTextViewDrawable(textView, 2);
            }

            textView.setEnabled(builder.isFieldEnable());
        }
    }

    public static void initEditText(EditText editText, FieldView.Builder builder) {
        initEditText(editText, builder, false);
    }

    public static void initEditText(EditText editText, FieldView.Builder builder, boolean showMustIcon) {
        if (builder != null) {
            if (!StringUtils2.isEmpty(builder.getValueInitContent())) {
                int length = builder.getValueInitContent().length();
                editText.setText(builder.getValueInitContent());
                editText.setSelection(length);
            }
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.getValueTextSize());
            if (!StringUtils2.isEmpty(builder.getEdittextHint())) {
                editText.setHint(builder.getEdittextHint());
            }
            if (builder.getEdittextLine() != -1) {
                editText.setLines(builder.getEdittextLine());
            }
            editText.setTextColor(ContextCompat.getColor(Utils.getApp(), builder.getValueTextColor()));
            editText.setBackgroundResource(builder.getDataBgColor());

            if (showMustIcon) {
                modifyTextViewDrawable(editText, 2);
            }

            editText.setEnabled(builder.isFieldEnable());
        }
    }

    public static void modifyTextViewDrawable(TextView v, int index) {
        Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), R.mipmap.ic_red_star);
        StringUtils2.modifyTextViewDrawable(v, drawable, index);
    }
}
