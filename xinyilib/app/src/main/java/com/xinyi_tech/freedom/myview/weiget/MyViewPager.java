package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.xinyi_tech.comm.log.XinYiLog;

/**
 * Created by zhiren.zhang on 2018/4/26.
 */

public class MyViewPager extends ViewGroup {

    private Scroller mScroller;
    private int mScaledPagingTouchSlop;
    private float mTouchStartX;
    private int mLeftBorder;
    private int mRightBorder;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mScaledPagingTouchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //是否拦截
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = ev.getX();
                float disX = Math.abs(curX - mTouchStartX);
                if (disX > mScaledPagingTouchSlop) {
                    //是移动操作，应该拦截
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                int scrolledX = (int) (mTouchStartX - curX);
                XinYiLog.e("onTouchEvent1" + "  getScrollX()==" + getScrollX() + "curX==" + curX + "scrolledX==" + scrolledX + "getWidth==" + getWidth() + "getLeft()==" + getLeft() + "getRight()=" + getRight());
                if (getScrollX() + scrolledX < mLeftBorder) {
                    scrollTo(mLeftBorder, 0);
                    return true;
                }
                if (getScrollX() + getRight() + scrolledX > mRightBorder) {
                    scrollTo(mRightBorder - getRight(), 0);
                    return true;
                }
                XinYiLog.e("onTouchEvent2" + "  getScrollX()==" + getScrollX() + "curX==" + curX + "scrolledX==" + scrolledX);
                scrollBy(scrolledX, 0);
                mTouchStartX = curX;
                //防止滑动过头
                break;
            case MotionEvent.ACTION_UP:
                //指定到对应的pager
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                XinYiLog.e("onTouchEvent3" + "  targetIndex==" + targetIndex + "dx==" + dx);
                //滚动到指定的位置
                mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        XinYiLog.e("computeScrollonTouchEvent4");
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();//反复调用computeScroll，因为computeScrollOffset不是益善啊完成的
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        XinYiLog.e("onSizeChanged  w=" + w + " h=" + h + " oldw=" + oldw + " oldh=" + oldh);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        XinYiLog.e("onSizeChanged  l=" + l + " t=" + t + " oldl=" + oldl + " oldt=" + oldt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XinYiLog.e("onMeasure");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        XinYiLog.e("onLayout");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.layout(i * childAt.getMeasuredWidth(), 0, (i + 1) * childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
        }

        mLeftBorder = getChildAt(0).getLeft();
        mRightBorder = getChildAt(childCount - 1).getRight();
    }
}
