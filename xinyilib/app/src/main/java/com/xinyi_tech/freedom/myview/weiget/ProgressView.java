package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/5/16.
 */

public class ProgressView extends View {

    float sweepAngle = 270;

    private Paint mArcPaint;
    SweepGradient mSweepGradient;
    private PointF mCenterPointF;
    private float mRadius;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = new RectF(mCenterPointF.x - mRadius, mCenterPointF.y - mRadius, mCenterPointF.x + mRadius, mCenterPointF.y + mRadius);

        canvas.drawArc(rectF, 90 +( 360 - sweepAngle)/2, sweepAngle, false, mArcPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateArcPaint();
    }

    private void updateArcPaint() {
        int measuredWidth = getMeasuredWidth();

        int measuredHeight = getMeasuredHeight();

        mCenterPointF = new PointF(measuredWidth / 2, measuredHeight / 2);

        mRadius = measuredWidth / 4;

        // 设置渐变

        int[] mGradientColors = {Color.GREEN, Color.YELLOW, Color.RED};

        mSweepGradient = new SweepGradient(mCenterPointF.x, mCenterPointF.y, mGradientColors, null);

        mArcPaint.setShader(mSweepGradient);

    }
}
