package com.xinyi_tech.freedom.myview.weiget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/5/16.
 */

public class PieChartView extends View {

    List<Float> datas = new ArrayList<>();
    int[] colors;
    private Paint piePaint;
    private Paint textPaint;
    private Paint linePaint;

    private int pieRadius;
    double touchAngle = -1;//点击的角度

    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        piePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        piePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(SizeUtils.sp2px(10));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);

        datas.add(30f);
        datas.add(20f);
        datas.add(15f);
        datas.add(10f);
        datas.add(20f);
        datas.add(5f);

        colors = new int[]{ResUtils.getColor(R.color.comm_red), ResUtils.getColor(R.color.comm_green),
                ResUtils.getColor(R.color.comm_orange), ResUtils.getColor(R.color.comm_blue),
                ResUtils.getColor(R.color.comm_grey), ResUtils.getColor(R.color.comm_pink), ResUtils.getColor(R.color.comm_yellow),
                ResUtils.getColor(R.color.comm_lime), ResUtils.getColor(R.color.comm_lightGreen), ResUtils.getColor(R.color.comm_teal)};


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isInCircle(event.getX(), event.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        int startX = -pieRadius;
        int startY = -pieRadius;
        int endX = pieRadius;
        int endY = pieRadius;


        float startAngle = 0;
        for (int i = 0, len = datas.size(); i < len; i++) {


            //绘制扇形
            piePaint.setColor(colors[i]);
            float sweepAngle = 360 * datas.get(i) / 100;
            if (touchAngle > startAngle && touchAngle < startAngle + sweepAngle) {
                startX -= 15;
                startY -= 15;
                endX += 15;
                endY += 15;
            } else {
                startX = -pieRadius;
                startY = -pieRadius;
                endX = pieRadius;
                endY = pieRadius;
            }
            RectF rectF = new RectF(startX, startY, endX, endY);
            canvas.drawArc(rectF, startAngle, sweepAngle - 1, true, piePaint);


            //绘制线
            float halfAngle = startAngle + (sweepAngle) / 2;
            double radians = Math.toRadians(halfAngle);
            float temp = (endX - startX) / 2;
            float halfX = (float) (Math.cos(radians) * temp);
            float halfY = (float) (Math.sin(radians) * temp);
            float halfEndX = (float) (Math.cos(radians) * (temp + 30));
            float halfEndY = (float) (Math.sin(radians) * (temp + 30));
            canvas.drawLine(halfX, halfY, halfEndX, halfEndY, linePaint);

            //绘制文字
            String percent = String.format("%.2f", datas.get(i)) + "%";
            if (halfAngle < 270 && halfAngle > 90) {
                float textWidth = textPaint.measureText(percent);
                canvas.drawText(percent, halfEndX - textWidth, halfEndY, textPaint);
            } else {
                canvas.drawText(percent, halfEndX, halfEndY, textPaint);
            }

            startAngle += sweepAngle;
        }

        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        pieRadius = Math.min(measuredWidth, measuredHeight) / 4;
    }

    private boolean isInCircle(float touchX, float touchY) {
        float touchWidth = touchX - pieRadius * 2;
        float touchHeight = touchY - pieRadius * 2;

        boolean isInCircle = Math.sqrt(Math.pow(Math.abs(touchWidth), 2) + Math.pow(Math.abs(touchHeight), 2)) < pieRadius;

        if (isInCircle) {
            // touchAngle= getTouchAngle(touchWidth,touchHeight);
            touchAngle = Math.toDegrees(Math.atan(touchHeight / touchWidth));
            if (touchWidth < 0 && touchHeight < 0) {//3
                touchAngle += 180;
            } else if (touchWidth < 0 && touchHeight > 0) {
                touchAngle += 180;//2
            } else if (touchWidth > 0 && touchHeight > 0) {
                touchAngle += 0;//1
            } else {
                touchAngle += 360;//4
            }

            invalidate();
        }

        return isInCircle;

    }

    public static float getTouchAngle(float x, float y) {
        float touchAngle = 0;
        if (x < 0 && y > 0) {  //2 象限
            touchAngle += 180;
        } else if (y < 0 && x > 0) {  //1象限
            touchAngle += 360;
        } else if (y > 0 && x < 0) {  //3象限
            touchAngle += 180;
        }
        //Math.atan(y/x) 返回正数值表示相对于 x 轴的逆时针转角，返回负数值则表示顺时针转角。
        //返回值乘以 180/π，将弧度转换为角度。
        touchAngle += Math.toDegrees(Math.atan(y / x));
        if (touchAngle < 0) {
            touchAngle = touchAngle + 360;
        }
        return touchAngle;
    }
}
