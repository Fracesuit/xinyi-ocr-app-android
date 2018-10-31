package com.xinyi_tech.freedom.myview.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice8DrawArcView extends View {

    public Practice8DrawArcView(Context context) {
        super(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawArc(new RectF(100, 100, 1000, 600), -110, 100, true, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(new RectF(100, 100, 1000, 600), 10, 150, false, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(100, 100, 1000, 600), 200, 30, false, paint);
//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
    }
}
