package com.xinyi_tech.freedom.myview.weiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;

/**
 * Created by zhiren.zhang on 2018/4/28.
 */

public class WaveView extends View {
    int zf = 100;

    Paint mPaint = new Paint();
    Paint cPaint = new Paint();
    Paint textPaint = new Paint();


    Path mPath = new Path();
    Path cPath = new Path();

    int progress;
    private int offset;//左右偏移量
    int waveHeight;//水位高度
    private ValueAnimator waveAnimator;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        initPath();
    }

    private void initPaints() {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        cPaint.setColor(Color.RED);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setStrokeWidth(5);
        cPaint.setAntiAlias(true);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(SizeUtils.sp2px(14));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initPath() {
        cPath.setFillType(Path.FillType.WINDING);
    }

    private void initAnimator(int width) {
        waveAnimator = ValueAnimator.ofInt(0, width);
        waveAnimator.setDuration(1000);
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //waveAnimator.setInterpolator(new FastOutSlowInInterpolator());
        waveAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    public void setProgress(int progress) {
        ValueAnimator waterLevelAnimator = ValueAnimator.ofInt(0, progress);
        waterLevelAnimator.setDuration(2000);
        waterLevelAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WaveView.this.progress = (int) animation.getAnimatedValue();
                waveHeight = getWaveHeight(WaveView.this.progress);
                invalidate();
            }
        });
        waterLevelAnimator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(SizeUtils.dp2px(300), MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(SizeUtils.dp2px(300), MeasureSpec.EXACTLY);
        }
        XinYiLog.e("WaveView onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        XinYiLog.e("WaveView onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //圆形
        cPath.reset();
        cPath.addCircle(circleX, circleY, circleRadius, Path.Direction.CW);
        canvas.clipPath(cPath);
        canvas.drawPath(cPath, cPaint);

        //波浪
        mPath.reset();
        mPath.moveTo(-width, waveHeight);
        mPath.quadTo(-width * 3 / 4 + offset, waveHeight - zf, -width / 2 + offset, waveHeight);
        mPath.quadTo(-width / 4 + offset, waveHeight + zf, offset, waveHeight);
        mPath.quadTo(width / 4 + offset, waveHeight - zf, width / 2 + offset, waveHeight);
        mPath.quadTo(width * 3 / 4 + offset, waveHeight + zf, width + offset, waveHeight);
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        if (waveAnimator != null && !waveAnimator.isStarted()) {
            waveAnimator.start();
        }
        if (progress == 0 || progress == 100) {
            waveAnimator.cancel();
        }

        //文字
        String text = progress + "%";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float offsetY = (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText(text, circleX, circleY + offsetY, textPaint);
    }

    int circleX, circleY, circleRadius;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        XinYiLog.e("WaveView onSizeChanged" + w);
        super.onSizeChanged(w, h, oldw, oldh);
        circleX = w / 2;
        circleY = h / 2;
        circleRadius = Math.min(w, h) / 2 - 100;
        waveHeight = getWaveHeight(progress);
        initAnimator(w);
    }

    private int getWaveHeight(int progress) {
        int waveHeight = (int) ((circleY + circleRadius) * (1 - (float) progress / 100));
        if (progress == 0) {
            waveHeight += zf;
        }
        XinYiLog.e("waveHeight==" + waveHeight);
        return waveHeight;

    }

    @Override
    protected void onDetachedFromWindow() {
        if (waveAnimator != null && waveAnimator.isStarted()) {
            waveAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }
}
