package com.xinyi_tech.freedom.myview.weiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.freedom.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhiren.zhang on 2018/4/27.
 */

public class HorizontalExpandMenu extends ViewGroup {

    private int defaultWidth;//默认宽度
    private int defaultHeight;//默认长度

    private int viewWidth;
    private int viewHeight;

    /**
     * 菜单背景
     */
    private int menuBackColor;//菜单栏背景色
    private int menuStrokeSize;//菜单栏边框线的size
    private int menuStrokeColor;//菜单栏边框线的颜色
    private float menuCornerRadius;//菜单栏圆角半径

    /**
     * 按钮
     */
    private float buttonIconDegrees = 90;//按钮icon符号竖线的旋转角度
    private float buttonIconSize;//按钮icon符号的大小
    private float buttonIconStrokeWidth;//按钮icon符号的粗细
    private int buttonIconColor;//按钮icon颜色

    private int buttonStyle;//按钮类型


    private Paint iconPaint;
    private MenuAnimatorUpdateListener mMenuAnimatorUpdateListener;
    private int menuLeft;
    private int menuRight;

    public HorizontalExpandMenu(Context context) {
        super(context);
    }

    public HorizontalExpandMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        defaultWidth = SizeUtils.dp2px(200);
        defaultHeight = SizeUtils.dp2px(40);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalExpandMenu);
        menuBackColor = typedArray.getColor(R.styleable.HorizontalExpandMenu_menuBackColor, Color.WHITE);
        menuStrokeColor = typedArray.getColor(R.styleable.HorizontalExpandMenu_menuStrokeColor, Color.GRAY);
        menuStrokeSize = typedArray.getInt(R.styleable.HorizontalExpandMenu_menuStrokeSize, 1);
        menuCornerRadius = typedArray.getDimension(R.styleable.HorizontalExpandMenu_menuCornerRadius, SizeUtils.dp2px(20));

        buttonIconSize = typedArray.getDimension(R.styleable.HorizontalExpandMenu_buttonIconSize, -1f);
        buttonIconColor = typedArray.getColor(R.styleable.HorizontalExpandMenu_menuStrokeColor, Color.GRAY);
        buttonIconStrokeWidth = typedArray.getInt(R.styleable.HorizontalExpandMenu_menuStrokeSize, 2);
        buttonStyle = typedArray.getInt(R.styleable.HorizontalExpandMenu_buttonIconStyle, ButtonStyle.LEFT);

        iconPaint = new Paint();
        iconPaint.setColor(buttonIconColor);
        iconPaint.setStrokeWidth(buttonIconStrokeWidth);
        iconPaint.setStyle(Paint.Style.STROKE);
        iconPaint.setAntiAlias(true);

        setWillNotDraw(false);

        mMenuAnimatorUpdateListener = new MenuAnimatorUpdateListener();


    }

    private void setMenuBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(menuBackColor);
        gradientDrawable.setStroke(menuStrokeSize, menuStrokeColor);
        gradientDrawable.setCornerRadius(menuCornerRadius);
        setBackground(gradientDrawable);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (getBackground() == null) {
            setMenuBackground();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buttonStyle == ButtonStyle.LEFT) {
            drawLeftIconButton(canvas);
        } else {
            drawRightIconButton(canvas);
        }

    }

    float downX;
    float downY;

    boolean isExpand = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float curX = event.getX();
                float curY = event.getY();
                if (downX == curX && downY == curY) {
                    RectF rectF = buttonStyle == ButtonStyle.LEFT ? getLeftIconRectF() : getRightIconRectF();
                    if (rectF.contains(curX, curY)) {
                        isExpand = !isExpand;
                        ValueAnimator iconAnimator;
                        if (isExpand) {
                            //展开
                            iconAnimator = ValueAnimator.ofFloat(0f, 90f);
                        } else {
                            //收缩
                            iconAnimator = ValueAnimator.ofFloat(90f, 0f);
                        }
                        iconAnimator.addUpdateListener(mMenuAnimatorUpdateListener);
                        iconAnimator.setDuration(1000);
                        iconAnimator.start();
                    }
                }
                break;
        }

        return true;
    }

    private RectF getLeftIconRectF() {
        return new RectF(0, 0, viewHeight, viewHeight);
    }

    private RectF getRightIconRectF() {
        return new RectF(viewWidth - viewHeight, 0, viewWidth, viewHeight);
    }

    private void drawLeftIconButton(Canvas canvas) {
        Point centerPoint = new Point(viewHeight / 2, viewHeight / 2);
        //横线
        canvas.drawLine(centerPoint.x - buttonIconSize / 2, centerPoint.y, centerPoint.x + buttonIconSize / 2, centerPoint.y, iconPaint);
        if (buttonIconDegrees > 0) {
            canvas.save();
            canvas.rotate(buttonIconDegrees, centerPoint.x, centerPoint.y);//正方向转
            canvas.drawLine(centerPoint.x - buttonIconSize / 2, centerPoint.y, centerPoint.x + buttonIconSize / 2, centerPoint.y, iconPaint);
            canvas.restore();
        }

    }

    private void drawRightIconButton(Canvas canvas) {
        Point centerPoint = new Point(viewWidth - viewHeight / 2, viewHeight / 2);
        //横线
        canvas.drawLine(centerPoint.x - buttonIconSize / 2, centerPoint.y, centerPoint.x + buttonIconSize / 2, centerPoint.y, iconPaint);
        if (buttonIconDegrees > 0) {
            canvas.save();
            canvas.rotate(buttonIconDegrees, centerPoint.x, centerPoint.y);
            canvas.drawLine(centerPoint.x - buttonIconSize / 2, centerPoint.y, centerPoint.x + buttonIconSize / 2, centerPoint.y, iconPaint);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = measureSize(defaultWidth, widthMeasureSpec);
        viewHeight = measureSize(defaultHeight, heightMeasureSpec);
        if (buttonIconSize == -1) {
            //按钮的长度为高度的1/3
            buttonIconSize = viewHeight / 3;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        menuLeft = getLeft();
        menuRight = getRight();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureSize(int defaultSize, int measureSpec) {
        XinYiLog.e("111 measureSize");
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        if (childCount != 1)
            throw new IllegalStateException("HorizontalExpandMenu can host only one direct child");
        View childView = getChildAt(0);
        if (isExpand) {
            if (buttonStyle == ButtonStyle.LEFT) {
                childView.layout(viewHeight, 0, r - viewHeight, viewHeight);
            } else {
                childView.layout(l + viewHeight, 0, viewWidth - viewHeight, viewHeight);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
    }

    @IntDef({
            ButtonStyle.LEFT,
            ButtonStyle.RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonStyle {
        int LEFT = 1;//线性
        int RIGHT = 2;//网格
    }

    class MenuAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            buttonIconDegrees = (float) animation.getAnimatedValue();
            float animatedFraction = animation.getAnimatedFraction();

            if (buttonStyle == ButtonStyle.LEFT) {
                float dRight = !isExpand ? (menuRight - animatedFraction * (menuRight - viewHeight)) : (viewHeight + animatedFraction * (menuRight - viewHeight));
                layout(getLeft(), getTop(), (int) dRight, getBottom());
            } else {
                int dLeft = (int) (!isExpand ? ((menuRight - viewHeight) * animatedFraction) : (int) ((menuRight - viewHeight) * (1 - animatedFraction) + menuLeft));
                layout(dLeft, getTop(), getRight(), getBottom());
            }
            invalidate();
        }
    }

}
