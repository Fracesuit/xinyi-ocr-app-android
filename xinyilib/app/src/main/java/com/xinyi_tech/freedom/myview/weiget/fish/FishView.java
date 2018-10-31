package com.xinyi_tech.freedom.myview.weiget.fish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xinyi_tech.comm.util.ResUtils;
import com.xinyi_tech.freedom.R;

import java.util.Random;

/**
 * Created by zhiren.zhang on 2018/5/11.
 */

public class FishView extends RelativeLayout {
    public static final int STROKE_WIDTH = 8;
    public static final float DEFAULT_RADIUS = 150;

    private ImageView ivFish;
    private FishDrawable fishDrawable;
    private ValueAnimator animator;
    private ObjectAnimator rippleAnimator;
    private Paint mPaint;
    private int alpha = 100;
    private Canvas canvas;

    private float x = 0;
    private float y = 0;
    private float radius = 0;

    public FishView(@NonNull Context context) {
        super(context);
    }

    public FishView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initStuff(context);
    }

    private void initStuff(Context context) {
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);


        ivFish = new ImageView(context);
        ivFish.setBackgroundColor(ResUtils.getColor(R.color.comm_green200));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(params);
        fishDrawable = new FishDrawable();
        ivFish.setImageDrawable(fishDrawable);

        addView(ivFish);


        rippleAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, 1f).setDuration(1000);
        rippleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //鱼儿开始游动
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //方便刷新透明度
        mPaint.setARGB(alpha, 0, 125, 251);
        canvas.drawCircle(x, y, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        rippleAnimator.start();
        makeTrail(new PointF(x, y));
        return true;
    }

    private void makeTrail(PointF touch) {
        /**
         * 起点和中点是鱼的身体重心处和点击点，三阶贝塞尔的两个控制点分别是鱼头圆心点和夹角中点。
         * 但是平移不是平移的鱼，而是平移的ImageView，而ImageView的setX，setY方法是相对于ImageView左上角的
         * 所以需要把计算好的贝塞尔曲线做一个平移，让贝塞尔曲线的起点和ImageView重合
         * deltaX,Y即是平移的绝对距离
         */
        float deltaX = fishDrawable.getMiddlePoint().x;
        float deltaY = fishDrawable.getMiddlePoint().y;
        Path path = new Path();
        PointF fishMiddle = new PointF(ivFish.getX() + deltaX, ivFish.getY() + deltaY);
        PointF fishHead = new PointF(ivFish.getX() + fishDrawable.getHeadPoint().x, ivFish.getY() + fishDrawable.getHeadPoint().y);
        //把贝塞尔曲线起始点平移到Imageview的左上角
        path.moveTo(ivFish.getX(), ivFish.getY());
        final float angle = includedAngle(fishMiddle, fishHead, touch);
        float delta = calcultatAngle(fishMiddle, fishHead);//delta是鱼身的角度
        PointF controlF = calculatPoint(fishMiddle, 1.6f * fishDrawable.HEAD_RADIUS, angle / 2 + delta);
        // PointF controlF = calculatPoint(fishMiddle, 1.6f * fishDrawable.HEAD_RADIUS, angle / 2 );
        //把贝塞尔曲线的所有控制点和结束点都做平移处理
        path.cubicTo(fishHead.x - deltaX, fishHead.y - deltaY, controlF.x - deltaX, controlF.y - deltaY, touch.x - deltaX, touch.y - deltaY);

        final float[] tan = new float[2];
        final float[] pos = new float[2];
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        //一个view根据path来进行移动  这里的处理方式对我学习还是蛮大的

        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        animator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        // animator = ObjectAnimator.ofFloat(ivFish, "x", "y", path);//此处可以使用估值器来进行移动
        animator.setDuration(2 * 1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        //动画启动和结束时设置鱼鳍摆动动画，同时控制鱼身摆动频率
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fishDrawable.setWaveFrequence(1f);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fishDrawable.setWaveFrequence(2f);
                ObjectAnimator finsAnimator = fishDrawable.getFinsAnimator();
                finsAnimator.setRepeatCount(new Random().nextInt(3));
                finsAnimator.setDuration((long) ((new Random().nextInt(1) + 1) * 500));
                finsAnimator.start();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, pos, tan);
                float angle = (float) (Math.toDegrees(Math.atan2(-tan[1], tan[0])));
                Log.e("**-**-**-**", "onAnimationUpdate: " + angle);
                fishDrawable.setMainAngle(angle);
                ivFish.setTranslationX(pos[0]);
                ivFish.setTranslationY(pos[1]);
            }
        });
        animator.start();

    }

    /**
     * ObjectAnimators自动执行
     *
     * @param currentValue
     */
    public void setRadius(float currentValue) {
        alpha = (int) (100 * (1 - currentValue) / 2);
        radius = DEFAULT_RADIUS * currentValue;
        invalidate();

    }

    /**
     * 线和x轴夹角
     *
     * @param start
     * @param end
     * @return
     */
    public static float calcultatAngle(PointF start, PointF end) {
        return includedAngle(start, new PointF(start.x + 1, start.y), end);
    }

    /**
     * 起点\长度\角度计算终点
     * 正逆负顺
     *
     * @param startPoint
     * @param length
     * @param angle
     * @return
     */
    private static PointF calculatPoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) Math.cos(Math.toRadians(angle)) * length;
        //符合Android坐标的y轴朝下的标准
        float deltaY = (float) Math.sin(Math.toRadians(angle - 180)) * length;
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    /**
     * 利用向量的夹角公式计算夹角
     * cosBAC = (AB*AC)/(|AB|*|AC|)
     * 其中AB*AC是向量的数量积AB=(Bx-Ax,By-Ay)  AC=(Cx-Ax,Cy-Ay),AB*AC=(Bx-Ax)*(Cx-Ax)+(By-Ay)*(Cy-Ay)
     *
     * @param center 顶点 A
     * @param head   点1  B
     * @param touch  点2  C
     * @return
     */
    public static float includedAngle(PointF center, PointF head, PointF touch) {
        float abc = (head.x - center.x) * (touch.x - center.x) + (head.y - center.y) * (touch.y - center.y);
        float angleCos = (float) (abc /
                ((Math.sqrt(Math.pow(head.x - center.x, 2) + Math.pow(head.y - center.y, 2)))
                        * (Math.sqrt(Math.pow(touch.x - center.x, 2) + Math.pow(touch.y - center.y, 2)))));

        float temAngle = (float) Math.toDegrees(Math.acos(angleCos));
        //判断方向  正:左侧  负:右侧 0:线上,但是Android的坐标系Y是朝下的，所以左右颠倒一下
        float direction = (center.x - touch.x) * (head.y - touch.y) - (center.y - touch.y) * (head.x - touch.x);
        //线上还要判断是同向还是逆向
        if (direction == 0) {
            if (abc >= 0) {
                return 0;
            } else
                return 180;
        } else {
            if (direction > 0) {//右侧顺时针为负
                return -temAngle;
            } else {
                return temAngle;
            }
        }
    }


}
