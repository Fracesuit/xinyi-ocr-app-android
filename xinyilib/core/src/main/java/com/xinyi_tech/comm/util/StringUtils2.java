package com.xinyi_tech.comm.util;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fracesuit on 2017/8/18.
 */

public class StringUtils2 {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "null".equalsIgnoreCase(str);
    }

    public static boolean isEmpty(Object str) {
        if (str == null) {
            return true;
        }
        if (str instanceof String) {
            String s = (String) str;
            return isEmpty(s);
        }
        return false;
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        if (!isEmpty(srcText)) {
            Pattern p = Pattern.compile(findText);
            Matcher m = p.matcher(srcText);
            while (m.find()) {
                count++;
            }
        }

        return count;
    }


    public static String getNumberFromStr(String content){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return  m.replaceAll("").trim();
    }

    public static void modifyTextViewDrawable(TextView v, Drawable drawable, int index) {
        if(drawable!=null)
        {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }

        //index 0:左 1：上 2：右 3：下
        if (index == 0) {
            v.setCompoundDrawables(drawable, null, null, null);
        } else if (index == 1) {
            v.setCompoundDrawables(null, drawable, null, null);
        } else if (index == 2) {
            v.setCompoundDrawables(null, null, drawable, null);
        } else {
            v.setCompoundDrawables(null, null, null, drawable);
        }
    }

    public static boolean isChinese(String str){
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if(m.find())

            return true;

        else

            return false;

    }
}
