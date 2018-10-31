package com.camera.widgets.cover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.luck.picture.lib.R;


/**
 * Created by Fracesuit on 2017/7/14.
 */
public class SquareCoverView extends View {
    private Paint mPaint;
    private Paint mPaintLine;
    private final String DEFAULT_TIPS_TEXT = "请将头像放入方框内部";
    private static final int DEFAULT_TIPS_TEXT_SIZE = 18;
    private static final int DEFAULT_TIPS_TEXT_COLOR = Color.RED;
    /**
     * 自定义属性
     */
    private float tipTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIPS_TEXT_SIZE, getResources().getDisplayMetrics());
    private int tipTextColor = DEFAULT_TIPS_TEXT_COLOR;
    private String tipText = DEFAULT_TIPS_TEXT;


    public SquareCoverView(Context context) {
        super(context);
        init();
    }


    public SquareCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SquareCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * 初始化绘图变量
     */
    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPaintLine = new Paint();
        this.mPaintLine.setColor(tipTextColor);
        this.mPaintLine.setStrokeWidth(3.0F);
        setKeepScreenOn(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCover(canvas);
        super.onDraw(canvas);
    }


    /**
     * 绘制取景框
     */
    private void drawCover(Canvas mCanvas) {
        try {
            mCanvas.drawARGB(40, 0, 0, 0);
            RectF scanRectF = getScanRectF();
            mCanvas.drawRect(scanRectF, this.mPaint);

            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_1);
            int mBitmapWidth = mBitmap.getWidth();
            /* 画四个角 */
            mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.scan_1), scanRectF.left, scanRectF.top, this.mPaintLine);// 左上边

            mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.scan_2), scanRectF.right - mBitmapWidth, scanRectF.top, this.mPaintLine);// 右上边

            mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.scan_3), scanRectF.left, scanRectF.bottom - mBitmapWidth, this.mPaintLine);// 左下边

            mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.scan_4), scanRectF.right - mBitmapWidth, scanRectF.bottom - mBitmapWidth, this.mPaintLine);// 右下边

            mPaintLine.setTextSize(tipTextSize);
            mPaintLine.setAntiAlias(true);
            mPaintLine.setDither(true);
            float length = mPaintLine.measureText(tipText);
            mCanvas.drawText(tipText, getMeasuredWidth() / 2 - length / 2, scanRectF.top - tipTextSize, mPaintLine);

            mCanvas.save();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public RectF getScanRectF() {
        //radio  w/h  ,一般都是小于1
        int mScreenW = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();

        float left = mScreenW / 8;

        float coverWidth = mScreenW - 2 * left;
        float coverHeight = (coverWidth * 4 / 3);

        float top = (screenHeight - coverHeight) / 2;
        float right = left + coverWidth;
        float bottom = (screenHeight + coverHeight) / 2;
        RectF rectF = new RectF(
                left,
                top,
                right,
                bottom);
        return rectF;

    }

}
