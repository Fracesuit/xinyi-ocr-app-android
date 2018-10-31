package com.camera.widgets.cover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.camera.configuration.Configuration;
import com.luck.picture.lib.R;

/**
 * Created by zhiren.zhang on 2018/4/23.
 */

public class SuperCoverView extends View {
    Paint paint = new Paint();
    Path path = new Path();
    RectF middleRectF;//中间的矩形框
    String message;//提示信息
    int coverType;

    public SuperCoverView(Context context, @Configuration.CoverType int coverType) {
        super(context);
        this.coverType = coverType;
    }

    public SuperCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectF getMiddleRectF() {
        return middleRectF;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (coverType) {
            case Configuration.COVERTYPE_FACE:
                drawFace(canvas);
                break;
            case Configuration.COVERTYPE_RECTANGLE:
                drawRectangle(canvas);//绘制矩形框
                break;
        }
    }

    private void drawRectangle(Canvas canvas) {

        middleRectF = getMiddleRect();

        path.setFillType(Path.FillType.INVERSE_WINDING);
        path.addRect(middleRectF, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(path);
        canvas.drawARGB(40, 0, 0, 0);
        canvas.restore();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        canvas.drawPath(path, paint);
    }

    public static RectF getMiddleRect() {
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();

        if (ScreenUtils.isPortrait()) {
            //竖屏
            int sleft = screenWidth / 20;
            int rectWidth = screenWidth - sleft * 2;
            int rectHeight = (int) (rectWidth / 1.6);
            return new RectF(sleft, (screenHeight - rectHeight) / 2, screenWidth - sleft, (screenHeight + rectHeight) / 2);

        } else {
            //横屏
            int sTop = screenHeight / 8;
            int rectHeight = screenHeight - 2 * sTop;
            int rectWidth = (int) (rectHeight * 1.6);
            return new RectF((screenWidth - rectWidth) / 2, sTop, (screenWidth + rectWidth) / 2, screenHeight - sTop);
        }
    }

    private void drawFace(Canvas canvas) {
        //必须是竖着的屏幕
        Rect clipBounds = canvas.getClipBounds();
        int coverViewWidth = clipBounds.right - clipBounds.left;
        int coverViewHeight = clipBounds.bottom - clipBounds.top;
        Bitmap bitmap = ImageUtils.getBitmap(R.drawable.camera_bg_face, coverViewWidth, coverViewHeight);
        Bitmap bitmap1 = ImageUtils.getBitmap(R.drawable.camera_bg_face);
        Bitmap bitmap2 = ImageUtils.getBitmap(R.drawable.camera_bg_face, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        Log.e("bitmap", "coverView==" + coverViewWidth + "  " + coverViewHeight);
        Log.e("bitmap", "ScreenUtils==" + ScreenUtils.getScreenWidth() + "  " + ScreenUtils.getScreenHeight());

        Log.e("bitmap", "bitmap==" + bitmap.getWidth() + "  " + bitmap.getHeight());
        Log.e("bitmap", "bitmap1==" + bitmap1.getWidth() + "  " + bitmap1.getHeight());
        Log.e("bitmap", "bitmap2==" + bitmap2.getWidth() + "  " + bitmap2.getHeight());


        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.camera_bg_face);
        Log.e("bitmap", "bitmap4==" + bitmap4.getWidth() + "  " + bitmap4.getHeight());
        // canvas.drawBitmap(bitmap4, 0, 0, paint);

        canvas.drawBitmap(bitmap4, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Rect(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()), paint);


    }
}
