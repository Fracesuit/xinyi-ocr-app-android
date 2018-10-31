package com.xinyi_tech.freedom.myview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;

/**
 * 1. activity的结构
 * 2. fitSystemWindows的用法含义
 * 3. https://www.jianshu.com/p/a8850e7cbac2  查看视图图层
 * 4.quadTo 贝塞尔曲线
 * 5. touch事件分发 dispatchTouchEvent 为true时，事件不在继续分发，而是自己处理
 * 6. getLocalVisibleRect  获取的视图的宽高(0,0,100,200)   getGlobalVisibleRect 获取视图在屏幕坐标系中的偏移量  (30,100,130,300)
 * 7.getX() 是表示Widget相对于自身左上角的x坐标，而getRawX()是表示相对于屏幕左上角的x坐标值(注意:这个屏幕左上角是手机屏幕左上角，不管activity是否有titleBar或是否全屏幕)； getY(),getRawY()一样的道理
 * <p>
 * <p>
 * <p>
 * Created by zhiren.zhang on 2018/9/7.
 */

public class SpringMenuView extends RelativeLayout {
    Activity activity;
    private ViewGroup contentView;

    public SpringMenuView(@NonNull Context context, @LayoutRes int layoutRes) {
        super(context);
        this.activity = (Activity) context;
        init(layoutRes);
    }

    private void init(int layoutRes) {
        initView(new MenuItemView(getContext(), layoutRes));

    }

    private void initView(View menuView) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        contentView = (ViewGroup) decorView.getChildAt(0);
        decorView.removeViewAt(0);
        addView(contentView);
        addView(menuView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        decorView.addView(this);
    }

    private void attachToActivity() {

    }


    @Override
    protected boolean fitSystemWindows(Rect insets) {
        //设置padding的值
       /* int bottomPadding = contentView.getPaddingBottom() + insets.bottom;
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasBackKey && !hasHomeKey) {
            bottomPadding += BarUtils.getStatusBarHeight();
        }*/
        this.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    private class MenuItemView extends FrameLayout {

        Path mArcPath;

        public MenuItemView(@NonNull Context context, @LayoutRes int layoutRes) {
            super(context);
            mArcPath = new Path();
            LayoutInflater.from(context).inflate(layoutRes, this);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    mArcPath.reset();
                    mArcPath.setFillType(Path.FillType.WINDING);
                    mArcPath.moveTo(0, 0);
                    mArcPath.quadTo(ev.getX(), ev.getY(), 0, ScreenUtils.getScreenHeight());
                    break;
                case MotionEvent.ACTION_UP:
                    mArcPath.reset();
                    mArcPath.moveTo(0, 0);
                  // mArcPath.setFillType(Path.FillType.INVERSE_WINDING);
                    float v = (float) (ScreenUtils.getScreenWidth() * 0.75);
                    mArcPath.lineTo(v, 0);
                    mArcPath.lineTo(v, ScreenUtils.getScreenHeight());
                    mArcPath.lineTo(0, ScreenUtils.getScreenHeight());

                    break;
            }
            mArcPath.close();
            invalidate();
            return true;
        }

        /**
         * Clipping Region from the path
         */
        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.save();
            canvas.clipPath(mArcPath);
            super.dispatchDraw(canvas);
            canvas.restore();
          /*  Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            canvas.drawPath(mArcPath,paint);*/
        }
    }
}
