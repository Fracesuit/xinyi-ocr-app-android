package com.xinyi_tech.comm.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.xinyi_tech.comm.constant.LayoutStyleCode.LAYOUT_GRID;
import static com.xinyi_tech.comm.constant.LayoutStyleCode.LAYOUT_LINEAR;
import static com.xinyi_tech.comm.constant.LayoutStyleCode.LAYOUT_LINEAR_H;
import static com.xinyi_tech.comm.constant.LayoutStyleCode.LAYOUT_STAGGERED;

/**
 * Created by Fracesuit on 2017/8/5.
 */

@IntDef({
        LAYOUT_LINEAR,
        LAYOUT_LINEAR_H,
        LAYOUT_GRID,
        LAYOUT_STAGGERED})
@Retention(RetentionPolicy.SOURCE)
public @interface LayoutStyleCode {
        int LAYOUT_LINEAR = 0;//线性
        int LAYOUT_LINEAR_H = 1;//线性  横向
        int LAYOUT_GRID = 2;//网格
        int LAYOUT_STAGGERED = 3;//瀑布
    }
