package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/5/16.
 */

public class TestView extends View {
    Paint strokeWPaint = new Paint();

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        strokeWPaint.setColor(Color.RED);
        strokeWPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //从这里可以看出  画笔是以中间为坐标的
        // 设置矩形空心
        strokeWPaint.setStrokeWidth(-10);
        canvas.drawRect(20, 320, 300, 600, strokeWPaint);
        // 设置矩形描边宽度
        strokeWPaint.setStrokeWidth(-40);
        canvas.drawRect(20, 650, 300, 900, strokeWPaint);
    }
}
