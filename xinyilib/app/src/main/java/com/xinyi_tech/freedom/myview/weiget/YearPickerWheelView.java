package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

/**
 * Created by zhiren.zhang on 2018/5/4.
 */

public class YearPickerWheelView extends View {
    int startYear = 1900;
    int endYear = 2100;
    private GestureDetector mGestureDetector;
    /**
     * Item的Text的颜色
     */
    @ColorInt
    private int mTextColor = ResUtils.getColor(R.color.comm_grey200);
    /**
     * 选中的Item的Text颜色
     */
    @ColorInt
    private int mSelectedItemTextColor = ResUtils.getColor(R.color.comm_blue500);

    private int mTextSize;
    private int mSelectedItemTextSize = SizeUtils.sp2px(16);

    /**
     * 最大的一个Item的文本的宽高
     */
    private int mTextMaxWidth, mTextMaxHeight;
    /**
     * 两个Item之间的高度间隔
     */
    private int mItemHeightSpace, mItemWidthSpace;


    public YearPickerWheelView(Context context) {
        super(context);
    }

    public YearPickerWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                XinYiLog.e("YearPickerWheelView onDown");
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                XinYiLog.e("YearPickerWheelView onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                XinYiLog.e("YearPickerWheelView onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                XinYiLog.e("YearPickerWheelView onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                XinYiLog.e("YearPickerWheelView onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                XinYiLog.e("YearPickerWheelView onFling");
                return false;
            }
        });

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //LinearGradient linearGradient = new LinearGradient(mTextColor, mSelectedItemTextColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
        // return super.onTouchEvent(event);
    }
}
