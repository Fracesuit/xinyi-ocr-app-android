package com.xinyi_tech.comm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fracesuit on 2017/8/28.
 */

public class CharacterUtils {
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
