package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/4/28.
 */

public class BlockLoadingDrawable extends View {

    private Paint mPaint;

    public BlockLoadingDrawable(Context context) {
        super(context);
        init();
    }

    public BlockLoadingDrawable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //方块属性
    private int blockColor= Color.GREEN;
    private int blockWidth=100;
    private float blockAngle=0;

    //排列
    private int hCount=3;//最少3个
    private int vCount=3;//最少3个
    private float hInterval=30;//水平间隔
    private float vInterval=30;//垂直间隔

    int curMovePosition=0;
    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(blockColor);

        //ValueAnimator.ofFloat(0,90)
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //大 方块的横向长度
        int blocksWidth = (int) (hCount * (blockWidth + hInterval) - hInterval);
        int blocksHeight = (int) (vCount * (blockWidth + vInterval) - vInterval);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(blocksWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(blocksHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int v = 0; v <= vCount; v++) {
            for (int h = 0; h <= hCount; h++) {
                float sx = h * (hInterval + blockWidth);
                float sy = v * (vInterval + blockWidth);
                canvas.drawRoundRect(new RectF(sx, sy, sx + blockWidth, sy + blockWidth), blockAngle, blockAngle, mPaint);
            }
        }


    }
}
