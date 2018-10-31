package com.xinyi_tech.freedom.myview.weiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.log.XinYiLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by zhiren.zhang on 2018/5/3.
 */

public class AntForestWaterView extends View {
    int waterCount = 5;//水滴的数量
    int waterRadius = 60;//水滴的半径

    int offset;

    private Paint waterPaint;
    private Paint textPaint;

    private List<Water> mWaters = new ArrayList<>();

    public AntForestWaterView(Context context) {
        super(context);
    }

    public AntForestWaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 200);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueAnimator.isStarted()) {
                    valueAnimator.cancel();
                } else {
                    valueAnimator.start();
                }
            }
        });


    }

    private void initPaint() {
        waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        waterPaint.setColor(Color.GREEN);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(SizeUtils.sp2px(16));
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < waterCount; i++) {
            mWaters.add(new Water());
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.save();
        //canvas.scale(getWidth()/2,getHeight()/2);
        for (Water water : mWaters) {
            float py = water.centerY + (water.moveUP ? -offset : offset)*water.moveSpeed;
            canvas.drawCircle(water.centerX, py, waterRadius, waterPaint);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float offsetY = (fontMetrics.descent + fontMetrics.ascent) / 2;
            canvas.drawText(water.text, water.centerX, py - offsetY, textPaint);
        }
        //canvas.restore();


    }


    class Water {
        /**
         * 控制水滴动画的快慢
         */
        private List<Float> mSpds = Arrays.asList(0.5f, 0.3f, 0.2f, 0.1f);

        private float centerX;
        private float centerY;

        private boolean moveUP;
        private float moveSpeed;
        private String text;

        public Water() {
            moveUP = getMoveDirection();
            getWaterPosition();
            text = getWaterText();
            moveSpeed = getMoveSpeed();
        }

        private void getWaterPosition() {
            int maxX = getWidth() - waterRadius;
            int maxY = getHeight() - waterRadius;
            int minX = waterRadius;
            int minY = waterRadius;

            Random sRandom = new Random();

            centerX = sRandom.nextInt(maxX - minX) + waterRadius;
            centerY = sRandom.nextInt(maxY - minY) + waterRadius;

            XinYiLog.e("Water centerX==" + centerX + "centerY==" + centerY);

            //return new RectF(sx, sy, ex, ey);
        }

        private float getMoveSpeed() {
            Random sRandom = new Random();
            return mSpds.get(sRandom.nextInt(mSpds.size()));
        }

        private boolean getMoveDirection() {
            Random sRandom = new Random();
            return sRandom.nextBoolean();
        }

        private String getWaterText() {
            Random sRandom = new Random();
            return sRandom.nextInt(100) + "g";
        }
    }
}
