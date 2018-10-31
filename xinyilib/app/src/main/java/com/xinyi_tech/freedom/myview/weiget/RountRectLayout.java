package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by zhiren.zhang on 2018/5/8.
 */

public class RountRectLayout extends RelativeLayout {
    int raidus = SizeUtils.dp2px(50);
    int leftTopRadius = raidus;
    int rightTopRadius = raidus;
    int leftBottomRadius = raidus;
    int rightBottomRadius = raidus;
    private Paint strokePaint;
    float[] radii = new float[8];
    private Path mPath;

    public RountRectLayout(Context context) {
        super(context);
    }

    public RountRectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.RED);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(SizeUtils.dp2px(3));

        mPath = new Path();

        radii[0] = leftTopRadius;
        radii[1] = leftTopRadius;
        radii[2] = rightTopRadius;
        radii[3] = rightTopRadius;
        radii[4] = leftBottomRadius;
        radii[5] = leftBottomRadius;
        radii[6] = rightBottomRadius;
        radii[7] = rightBottomRadius;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipPath(mPath);//裁剪画布
        super.draw(canvas);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
       // canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
       // strokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
      //  strokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
     //   canvas.drawPath(mPath, strokePaint);
     //   canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPath.addRoundRect(rectF, radii, Path.Direction.CW);
    }
}
