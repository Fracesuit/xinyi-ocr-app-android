package com.xinyi_tech.freedom.myview.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice9DrawPathView extends View {

    public Practice9DrawPathView(Context context) {
        super(context);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();

        path.addArc(new RectF(200, 200, 400, 400), -225, 225);
        path.arcTo(new RectF(400, 200, 600, 400), -180, 225, false);
        path.lineTo(400, 550);
        canvas.drawPath(path, new Paint());
//        练习内容：使用 canvas.drawPath() 方法画心形

        canvas.translate(100,200);//只有平移 旋转后才需要保存save canvas的位置
        Path dashPath = new Path();
        dashPath.lineTo(20, -30);//这里的位置都是相对位置
        dashPath.lineTo(40, 0);
        dashPath.close();
        canvas.drawPath(dashPath, new Paint());

    }
}
