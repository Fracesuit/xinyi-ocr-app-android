package com.xinyi_tech.freedom.myview.weiget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/4/27.
 */

public class RippleView extends View {
    Paint mPaint = new Paint();

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);


        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "changeRadius", 0, 100);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.start();
    }

    int changeRadius;

    public void setChangeRadius(int changeRadius) {
        this.changeRadius = changeRadius;
        invalidate();
        //requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawCircle(0, 0, changeRadius, mPaint);
        canvas.drawCircle(0, 0, changeRadius * 2, mPaint);
        canvas.drawCircle(0, 0, changeRadius * 3, mPaint);
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.makeMeasureSpec(100 * 6, MeasureSpec.EXACTLY));
    }
}
