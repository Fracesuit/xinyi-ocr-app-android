package com.xinyi_tech.freedom.myview.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhiren.zhang on 2018/5/9.
 */

public class XfermodeView extends View {
    Paint paint = new Paint();

    public XfermodeView(Context context) {
        super(context);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
         paint.setStyle(Paint.Style.FILL);
        //   paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
     //   canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
      //  canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
       // canvas.drawCircle(r, r, r, paint);
        //绘制蓝色的矩形
        paint.setColor(0xFF66AAFF);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形

       // paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        path.addRect(r, r, r * 2.7f, r * 2.7f, Path.Direction.CW);
        canvas.drawPath(path, paint);
      //  paint.setXfermode(null);
       // canvas.restore();
    }
    /**
     * 一般我们在调用canvas.drawXXX()方法时都会传入一个画笔Paint对象，Android在绘图时会先检查该画笔Paint对象有没有设置Xfermode，如果没有设置Xfermode，那么直接将绘制的图形覆盖Canvas对应位置原有的像素；如果设置了Xfermode，那么会按照Xfermode具体的规则来更新Canvas中对应位置的像素颜色。就本例来说，在执行canvas.drawCirlce()方法时，画笔Paint没有设置Xfermode对象，所以绘制的黄色圆形直接覆盖了Canvas上的像素。当我们调用canvas.drawRect()绘制矩形时，画笔Paint已经设置Xfermode的值为PorterDuff.Mode.CLEAR，此时Android首先是在内存中绘制了这么一个矩形，所绘制的图形中的像素称作源像素（source，简称src），所绘制的矩形在Canvas中对应位置的矩形内的像素称作目标像素（destination，简称dst）。源像素的ARGB四个分量会和Canvas上同一位置处的目标像素的ARGB四个分量按照Xfermode定义的规则进行计算，形成最终的ARGB值，然后用该最终的ARGB值更新目标像素的ARGB值。本例中的Xfermode是PorterDuff.Mode.CLEAR，该规则比较简单粗暴，直接要求目标像素的ARGB四个分量全置为0，即(0，0，0，0)，即透明色，所以我们通过canvas.drawRect()在Canvas上绘制了一个透明的矩形，由于Activity本身屏幕的背景时白色的，所以此处就显示了一个白色的矩形。
     */
}
