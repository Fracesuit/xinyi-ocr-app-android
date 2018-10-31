package com.xinyi_tech.freedom.myview.weiget.qqpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;

/**
 * Created by zhiren.zhang on 2018/5/15.
 */

public class StickView extends View {
    int text;
    private Paint textPaint;
    private Paint bgPaint;

    private int distanceMax = ScreenUtils.getScreenWidth() / 2;//最大移动距离
    private PointF dragPoint;//拖动的点位  及手指的点位
    private PointF stickPoint;//固定点位  及 初始点位
    private GestureDetector mGestureDetector;

    public StickView(Context context) {
        super(context);
    }

    public StickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(SizeUtils.sp2px(12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.RED);
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                StickView.this.getParent().requestDisallowInterceptTouchEvent(false);
                XinYiLog.e("StickView  onDown");
                return true;
            }


            @Override
            public void onShowPress(MotionEvent e) {
                XinYiLog.e("StickView  onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                XinYiLog.e("StickView  onSingleTapUp");
                StickView.this.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                StickView.this.getParent().requestDisallowInterceptTouchEvent(false);
                XinYiLog.e("StickView  onScroll");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                XinYiLog.e("StickView  onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                XinYiLog.e("StickView  onFling");
                return false;
            }
        });


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padding = SizeUtils.dp2px(6);
        float minWidth = textPaint.measureText("00") + padding;
        float minHeight = textPaint.descent() - textPaint.ascent() + padding;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) minWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) minHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setText(int text) {
        this.text = text;
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  q.rorbin.badgeview.MathUtil.
        if (text > 0) {
            int centerX = getMeasuredWidth() / 2;
            int centerY = getMeasuredHeight() / 2;
            float offsetY = (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawCircle(centerX, centerY, centerX, bgPaint);
            if (text > 99) text = 99;
            canvas.drawText(String.valueOf(text), centerX, centerY - offsetY, textPaint);
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
