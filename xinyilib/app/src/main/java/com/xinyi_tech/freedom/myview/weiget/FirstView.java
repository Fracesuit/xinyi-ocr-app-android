package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/4/16.
 */

public class FirstView extends View {
    Paint paint = new Paint();

    public FirstView(Context context) {
        super(context);
    }

    public FirstView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // this.paint.setAntiAlias(true);
        //this.paint.setColor(Color.RED);
        // this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void init() {
        /**
         * 设置画笔的风格  填充
         * Paint.Style.FILL：填充内部
         Paint.Style.FILL_AND_STROKE  ：填充内部和描边
         Paint.Style.STROKE  ：描边
         */
       // paint.setStyle(Paint.Style.FILL_AND_STROKE);//Paint.Style.FILL  Paint.Style.STROKE  Paint.Style.FILL_AND_STROKE

        /**
         * 设置线条两端的形状
         * Paint.Cap.BUTT  线条两端没有两端
         * Paint.Cap.ROUND   椭圆形
         * Paint.Cap.SQUARE  方形  ，比BUTT的要长点
         */
        //paint.setStrokeCap(Paint.Cap.ROUND);

        /**
         * 设置笔画连接点的形状
         * Paint.Join.BEVEL   连接是斜的，就像矩形角被切了一块
         * Paint.Join.MITER  连接是重叠的，端正的矩形
         * Paint.Join.ROUND  连接是原的，矩形的倒角
         */
        paint.setStrokeJoin(Paint.Join.ROUND);

        /**+
         * 设置连接线的倾斜度
         */
        paint.setStrokeMiter(1.5f);

        /**
         * 设置文字对其方式
         * Paint.Align.CENTER
         * Paint.Align.LEFT
         * Paint.Align.RIGHT
         */
        paint.setTextAlign(Paint.Align.RIGHT);

        /**
         * 重置Paint  ,就相当于之前的设置都是没用的
         */
        paint.reset();

        //这个可以设置很多东西
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界，如果设置，边界就会变的稍微有点模糊，锯齿就看不到了。
        paint.setAntiAlias(true);

        //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        paint.setDither(true);

        //这个是文本缓存，设置线性文本，如果设置为true就不需要缓存，
        paint.setLinearText(true);

//设置亚像素，是对文本的一种优化设置，可以让文字看起来更加清晰明显，可以参考一下PC端的控制面板-外观和个性化-调整ClearType文本

        paint.setSubpixelText(true);
//设置文本的下划线

        paint.setUnderlineText(true);
//设置文本的删除线

        paint.setStrikeThruText(true);
//设置文本粗体
        paint.setFakeBoldText(true);


        /**
         * 设置重叠模式
         * 有18种重叠模式
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  this.animate().translationX(15);
        // this.setTranslationX(55);
        //  canvas.drawARGB(40, 0, 0, 0);
        // canvas.drawCircle(100, 200, 50, paint);
        super.onDraw(canvas);
        //设置背景色
       /* canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int r = canvasWidth / 3;
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);*/

//        paint.setStyle(Paint.Style.FILL); // 填充模式
//        canvas.drawArc(new RectF(200, 100, 800, 500), -110, 90, true, paint); // 绘制扇形
//        canvas.drawArc(new RectF(200, 100, 800, 500), 20, 140, true, paint); // 绘制弧形
//        paint.setStyle(Paint.Style.STROKE); // 画线模式
//        canvas.drawArc(new RectF(200, 100, 800, 500), 180, 60, false, paint); // 绘制不封口的弧形
         paint.setColor(Color.parseColor("#40000000"));
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
       // path.addCircle(100,100,200,Path.Direction.CCW);
        //path.addCircle(400,400,300,Path.Direction.CCW);
       path.addRect(0, 0, 300,300, Path.Direction.CW);
       path.addRect(100, 100, 200, 200, Path.Direction.CCW);
        canvas.drawPath(path, paint);

    }
}
