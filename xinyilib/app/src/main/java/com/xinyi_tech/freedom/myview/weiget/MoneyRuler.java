package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

/**
 * Created by zhiren.zhang on 2018/4/26.
 */

public class MoneyRuler extends View {
    int interval = 38;//每小格之间的距离
    int maxScale = 1000;//最大的刻度值
    int minScale = 50;//最小的刻度值
    float scaleUnit = 0.5f;//比例因子 一个多少
    int count = 10;//每大格有多少小格
    float currentScale = 0;//当前的位置

    int minPosition, maxPosition, length;//最小可滑动的位置  长度==多少个像素

    private Paint mTextPaint;
    private Paint mSmallScalePaint;
    private Paint mBigScalePaint;
    private Paint mOutLinePaint;
    private Paint mCursorPaint;
    private Path mCursorPath;

    //光标宽度、高度
    private int mCursorWidth = 8, mCursorHeight = 70;
    //大小刻度的长度
    private int mSmallScaleLength = 30, mBigScaleLength = 60;
    //大小刻度的粗细
    private int mSmallScaleWidth = 3, mBigScaleWidth = 5;

    private int mTextBigScaleDistance = 60;
    //数字字体大小
    private int mTextSize = 28;
    private OverScroller mOverScroller;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;
    private int mScaledTouchSlop;
    private VelocityTracker mVelocityTracker;

    //光标的颜色
    //刻度尺的颜色
    //背景色
    //基准的颜色

    public MoneyRuler(Context context) {
        super(context);
        init();
    }

    public MoneyRuler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initPaints();

        mOverScroller = new OverScroller(getContext());
        mScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    private void initPaints() {
        //文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        //小刻度
        mSmallScalePaint = new Paint();
        mSmallScalePaint.setColor(ResUtils.getColor(R.color.comm_grey300));
        mSmallScalePaint.setStrokeWidth(mSmallScaleWidth);
        mSmallScalePaint.setStrokeCap(Paint.Cap.ROUND);
        //大刻度
        mBigScalePaint = new Paint();
        mBigScalePaint.setColor(ResUtils.getColor(R.color.comm_grey300));
        mBigScalePaint.setStrokeWidth(mBigScaleWidth);
        mBigScalePaint.setStrokeCap(Paint.Cap.ROUND);

        //基准线
        mOutLinePaint = new Paint();
        mOutLinePaint.setStrokeWidth(0);
        mOutLinePaint.setColor(ResUtils.getColor(R.color.comm_grey300));


        mCursorPaint = new Paint();
        mCursorPaint.setStyle(Paint.Style.STROKE);
        mCursorPaint.setStrokeCap(Paint.Cap.ROUND);
        mCursorPaint.setStrokeWidth(mCursorWidth);
        mCursorPaint.setColor(ResUtils.getColor(R.color.comm_orange));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制刻度尺
        int start = getScrollX() / interval+minScale;
        int end = (getScrollX() + canvas.getWidth()) / interval + minScale;
        XinYiLog.e("start" + start + "  end" + end);

        int height = getHeight();
        for (int scale = start; scale <= end; scale++) {
            if (scale >= minScale && scale <= maxScale) {
                int startX = (scale - minScale) * interval;
                if (scale % count == 0) {
                    canvas.drawLine(startX, height - mBigScaleLength, startX, height, mBigScalePaint);
                    //绘制文字
                    canvas.drawText(getAccuracyText(scale), startX, height - mBigScaleLength - mTextBigScaleDistance, mTextPaint);
                } else {
                    canvas.drawLine(startX, height - mSmallScaleLength, startX, height, mSmallScalePaint);
                }
            }

        }

        //绘制基准线
        canvas.drawLine(getScrollX(), canvas.getHeight() - 1, getScrollX() + canvas.getWidth(), canvas.getHeight() - 1, mOutLinePaint);

        //绘制光标
        mCursorPath = new Path();
        mCursorPath.moveTo(getScrollX() + canvas.getWidth() / 2, getHeight() - mCursorHeight);
        mCursorPath.lineTo(getScrollX() + canvas.getWidth() / 2, getHeight());
        canvas.drawPath(mCursorPath, mCursorPaint);
    }

    private String getAccuracyText(float scale) {
      /*  if (scaleUnit >= 1) {
            return String.valueOf((int) (scale * scaleUnit));
        } else if (scaleUnit > 0) {
            return String.valueOf(scale * scaleUnit);
        }*/
        return ""+scale;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        XinYiLog.e("onSizeChangedInner2");
        super.onSizeChanged(w, h, oldw, oldh);
        int halfWidth = getWidth() / 2;
        length = (maxScale - minScale) * interval;
        minPosition = -halfWidth;
        maxPosition = length - halfWidth;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), SizeUtils.dp2px(60));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    float downX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float dx = downX - moveX;//dx>0 说明手在屏幕上向左滑动，那么滚动条就要向右边滚动
                scrollBy((int) dx, 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > mScaledMinimumFlingVelocity) {
                    mOverScroller.fling(getScrollX(), 0, -xVelocity, 0, minPosition, maxPosition, 0, 0);
                } else {
                    //滚动到顶点标准位置
                    scrollBackToCurrentScale();
                }
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                releaseVelocityTracker();
                break;
        }

        return true;
    }

    private void scrollBackToCurrentScale() {
        mOverScroller.startScroll(getScrollX(), 0, scaleToScrollX(Math.round(currentScale)) - getScrollX(), 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            XinYiLog.e(" dd" + mOverScroller.computeScrollOffset() + "   currentScale" + currentScale + "  Math.round(currentScale)" + Math.round(currentScale));
            if (!mOverScroller.computeScrollOffset() && currentScale != Math.round(currentScale)) {
                scrollBackToCurrentScale();
            }
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (x < minPosition) {
            x = minPosition;
        } else if (x > maxPosition) {
            x = maxPosition;
        }
        if (getScrollX() != x) {
            currentScale = scrollXToScale(x);
            XinYiLog.e("当前的位置:" + currentScale * scaleUnit);
            super.scrollTo(x, y);
        }

    }


    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //根据当前的position计算出在滑动距离()
    private int scaleToScrollX(float scare) {
        return (int) ((scare - minScale) * length / (maxScale - minScale) + minPosition);
    }

    //根据屏幕上的滑动距离计算出刻度
    private float scrollXToScale(int scrollX) {
        return ((float) (scrollX - minPosition) * (maxScale - minScale)) / length + minScale;
    }
}
