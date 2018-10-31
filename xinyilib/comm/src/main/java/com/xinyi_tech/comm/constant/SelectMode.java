package com.xinyi_tech.comm.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.xinyi_tech.comm.constant.SelectMode.SELECTMODE_EDIT;
import static com.xinyi_tech.comm.constant.SelectMode.SELECTMODE_LOOK;
import static com.xinyi_tech.comm.constant.SelectMode.SELECTMODE_PLACEHOLDER;

/**
 * Created by Fracesuit on 2017/8/5.
 */

@IntDef({
        SELECTMODE_LOOK,
        SELECTMODE_EDIT,
        SELECTMODE_PLACEHOLDER})
@Retention(RetentionPolicy.SOURCE)
public @interface SelectMode {
    int SELECTMODE_LOOK = 0;//查看模式
    int SELECTMODE_EDIT = 1;//1修改添加模式
    int SELECTMODE_PLACEHOLDER = 2;//2占位模式
}
