package com.xinyi_tech.freedom.myview.weiget.qqpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.xinyi_tech.comm.log.XinYiLog;

/**
 * Created by zhiren.zhang on 2018/5/15.
 */

public class QQPointView extends android.support.v7.widget.AppCompatTextView {

    private DragView mDragView;

    public QQPointView(Context context) {
        super(context);
    }

    public QQPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QQPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup rootView = (ViewGroup) getRootView();
        XinYiLog.e("QQPointView rootView" + rootView.getClass());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                setDrawingCacheEnabled(true);
                mDragView = new DragView(getDrawingCache(), new PointF(event.getRawX(), event.getRawY()), getContext());
                rootView.addView(mDragView);
                setVisibility(INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                mDragView.update(new PointF(event.getRawX(), event.getRawY()));
                break;
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return true;
    }

    class DragView extends View {
        private PointF dragPointF, stickyPointF;//拖拽圆的圆点和固定圆的圆点
        private Paint mPaint;
        private Path mPath;
        private int stickRadius = 20;//固定圆的半径
        private int defaultRadius = 30;//固定小圆的默认半径
        private int dragRadius = 40;//拖拽圆的半径
        private Bitmap dragBitmap;

        public DragView(Bitmap bitmap, PointF dragPointF, Context context) {
            super(context);
            this.dragBitmap = bitmap;
            this.dragPointF = dragPointF;
            this.stickyPointF = dragPointF;
            dragRadius = Math.min(dragBitmap.getWidth(), dragBitmap.getHeight())/2;
            init();
        }

      /*  public DragView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }*/

        public void update(PointF dragPointF) {
            this.dragPointF = dragPointF;
            invalidate();
        }

        private void init() {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setDither(true);
            mPath = new Path();

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //绘制固定的小圆
            canvas.drawCircle(stickyPointF.x, stickyPointF.y, stickRadius, mPaint);
            canvas.drawBitmap(dragBitmap, dragPointF.x - dragRadius, dragPointF.y - dragRadius, mPaint);
            Float lineSlope = MathUtil.getLineSlope(dragPointF, stickyPointF);
            PointF[] dragPoints = MathUtil.getIntersectionPoints(dragPointF, dragRadius, lineSlope);
            PointF[] stickyPoints = MathUtil.getIntersectionPoints(stickyPointF, stickRadius, lineSlope);

            //二阶贝塞尔曲线的控制点取得两圆心的中点
            PointF controlPoint = MathUtil.getMiddlePoint(dragPointF, stickyPointF);
            //绘制贝塞尔曲线
            mPath.reset();
            mPath.moveTo(stickyPoints[0].x, stickyPoints[0].y);
            mPath.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);
            mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
            mPath.quadTo(controlPoint.x, controlPoint.y, stickyPoints[1].x, stickyPoints[1].y);
            mPath.lineTo(stickyPoints[0].x, stickyPoints[0].y);
            canvas.drawPath(mPath, mPaint);

        }
    }
}
