package com.xinyi_tech.comm.widget.picker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.camera.configuration.Configuration;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.form.IFormField;
import com.xinyi_tech.comm.permission.DefaultRequestPermissionsListener;
import com.xinyi_tech.comm.permission.PermissionsHelp;
import com.xinyi_tech.comm.picker.PickUtils;
import com.xinyi_tech.comm.util.ImageLoaderUtils;
import com.xinyi_tech.comm.util.ResUtils;

import java.util.List;


import static android.app.Activity.RESULT_OK;


public class SuperImageView extends AppCompatImageView implements View.OnClickListener, IFormField {

    private static final int SHAPE_RECTANGLE = 1;
    private static final int SHAPE_CYCLO = 2;

    private int frameWidth = -1;//支持设置padding值   如果padding！=-1 就代表要设置边框
    private int frameColor = R.color.comm_grey300;//默认边框颜色
    private boolean supportCache = true;//是否需要缓存
    private boolean supporCrop = false;//支持裁剪
    private int sImgCropX = 0;//裁剪比例
    private int sImgCropY = 0;//裁剪比例
    private boolean isLookMode = true;//是否是查看模式  查看模式不能有删除的按钮，点击可以放大。
    private boolean isOnlyCamera = false;//仅仅是拍照
    private boolean supportBigImage = true;
    private float ratio = -1;//支持是否在查看大图里改变
    private int shape = SHAPE_RECTANGLE; //1 是矩形  2是圆形
    private int imageHolder = 0;//开始时默认的图片
    @Configuration.CoverType
    private int converType = -1;//-1 系统  1 人脸 2 矩形 3 智能人脸
    @Configuration.MediaQuality
    private int mediaQuality;
    private int requestCode = PictureConfig.CHOOSE_REQUEST;


    Activity activity;
    private Drawable holderDrawable;

    private String imgPath;
    private String bigImgPath;

    public SuperImageView(Context context) {
        super(context);
        init(context, null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (parentWidthMode == MeasureSpec.EXACTLY && ratio != -1) {
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = (int) (childWidth / ratio + .5f);
            int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();
            setMeasuredDimension(parentWidth, parentHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperImageView);
            frameWidth = (int) typedArray.getDimension(R.styleable.SuperImageView_sImgFrameWidth, -1);
            frameColor = typedArray.getResourceId(R.styleable.SuperImageView_sImgFrameColor, R.color.comm_grey300);
            supportCache = typedArray.getBoolean(R.styleable.SuperImageView_sImgsupportCache, true);
            supporCrop = typedArray.getBoolean(R.styleable.SuperImageView_sImgSupporCrop, false);
            converType = typedArray.getInt(R.styleable.SuperImageView_sImgConverType, -1);
            mediaQuality = typedArray.getInt(R.styleable.SuperImageView_sImgMediaQuality, -1);
            sImgCropX = typedArray.getInt(R.styleable.SuperImageView_sImgCropX, 0);
            sImgCropY = typedArray.getInt(R.styleable.SuperImageView_sImgCropY, 0);
            requestCode = typedArray.getInt(R.styleable.SuperImageView_sImgRequestCode, PictureConfig.CHOOSE_REQUEST);
            isLookMode = typedArray.getBoolean(R.styleable.SuperImageView_sImgLookMode, true);
            supportBigImage = typedArray.getBoolean(R.styleable.SuperImageView_sImgSupportBigImage, true);
            isOnlyCamera = typedArray.getBoolean(R.styleable.SuperImageView_sImgOnlyCamera, false);
            ratio = typedArray.getFloat(R.styleable.SuperImageView_sImgRatio, -1);
            shape = typedArray.getInt(R.styleable.SuperImageView_sImgShape, 1);
            imageHolder = typedArray.getResourceId(R.styleable.SuperImageView_sImgImageHolder, 0);
            if (imageHolder != 0) {
                setImageResource(imageHolder);
                holderDrawable = getDrawable();
            }
            typedArray.recycle();
        }
        super.setOnClickListener(this);

    }


    public SuperImageView with(Activity activity) {
        this.activity = activity;
        return this;
    }

    public void show(Object imgUrl) {
        if (imgUrl instanceof Integer) {
            this.setImageResource((Integer) imgUrl);
        } else if (imgUrl instanceof String) {
            show((String) imgUrl);
        } else {
            clearImage();
        }
    }

    private void show(String imgUrl) {
        if (!StringUtils.isEmpty(imgUrl)) {
            this.imgPath = imgUrl;
            final RequestOptions requestOptions = new RequestOptions();
            if (shape == 2) {
                requestOptions.apply(RequestOptions.circleCropTransform());
            }
            requestOptions.skipMemoryCache(!supportCache)
                    .diskCacheStrategy(supportCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
            ImageLoaderUtils.showImage(this, imgUrl, 0, requestOptions);
        }
    }

    public String getImgPath() {
        return imgPath;
    }

    public SuperImageView setImgPath(String imgPath) {
        this.imgPath = imgPath;
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new RuntimeException("super不支持直接设置单击事件,可以设置setOnImageViewClickListener");
    }

    @Override
    public void onClick(View v) {
        if (onImageViewClickListener != null) {
            if (onImageViewClickListener.onClick(!TextUtils.isEmpty(imgPath))) {
                return;
            }
        }
        //有图片的时候就是查看大图
        if (!TextUtils.isEmpty(imgPath) && supportBigImage) {
            //查看大图
            ImageLoaderUtils.showBigImage(bigImgPath == null ? imgPath : bigImgPath);
        } else {
            if (activity != null) {
                if (isOnlyCamera) {
                    startCamera();
                } else {
                    startGallery();
                }
            }

        }
    }

    public void startCamera() {
        PermissionsHelp.with(activity).requestPermissions(new DefaultRequestPermissionsListener() {
            @Override
            public void grant() {
                @SuppressLint("WrongConstant")
                PictureSelectionModel camera = PickUtils.camera(activity, PictureMimeType.ofImage())
                        .setCustomCameraMediaQuality(mediaQuality)
                        .setCustomCameraCoverType(converType);
                if (supporCrop) {
                    PickUtils.crop(camera, sImgCropX, sImgCropY, shape == SHAPE_CYCLO);
                }
                PickUtils.pick(camera, 1, null, requestCode);
            }
        }, PermissionsHelp.CAMERA, PermissionsHelp.WRITE_EXTERNAL_STORAGE);
    }


    public void startGallery() {
        PermissionsHelp.with(activity).requestPermissions(new DefaultRequestPermissionsListener() {
            @Override
            public void grant() {
                @SuppressLint("WrongConstant")
                PictureSelectionModel camera = PickUtils.gallery(activity, PictureMimeType.ofImage())
                        .setCustomCameraMediaQuality(mediaQuality)
                        .setCustomCameraCoverType(converType);
                if (supporCrop) {
                    PickUtils.crop(camera, sImgCropX, sImgCropY, shape == SHAPE_CYCLO);
                }

                PickUtils.pick(camera, 1, null, requestCode);
            }
        }, PermissionsHelp.CAMERA, PermissionsHelp.WRITE_EXTERNAL_STORAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode && data != null && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                final LocalMedia localMedia = selectList.get(0);
                show(localMedia.isCompressed() ? localMedia.getCompressPath() : localMedia.isCut() ? localMedia.getCutPath() : localMedia.getPath());
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (frameWidth != -1) {
            Paint paint = new Paint();
            paint.setColor(ResUtils.getColor(frameColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(frameWidth);
            paint.setAntiAlias(true);
            if (shape == 1) {
                //获取控件需要重新绘制的区域
                Rect rect = canvas.getClipBounds();
                canvas.drawRect(rect, paint);
            } else {

                int w = this.getPaddingLeft() + this.getPaddingRight();
                int x = getWidth() - w;
                int r = x / 2;
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, r - frameWidth / 2, paint);
            }
        }
        //不需要显示删除按钮的情况
        if (!StringUtils.isEmpty(imgPath) && !isLookMode) {
            int width = SizeUtils.dp2px(20);
            Rect rect = canvas.getClipBounds();
            Paint mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitPaint.setFilterBitmap(true);
            mBitPaint.setDither(true);
            final Bitmap bitmap = ImageUtils.getBitmap(R.mipmap.icon_right_up_delete);
            final Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final Rect destRect = new Rect(rect.right - width, 0, rect.right, width);
            canvas.drawBitmap(bitmap, srcRect, destRect, mBitPaint);
        }

    }


    float startDownX;
    float startDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startDownX = event.getX();
                startDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                final float upX = event.getX();
                final float upY = event.getY();
                final float dx = Math.abs(startDownX - upX);
                final float dy = Math.abs(startDownY - upY);
                if (!StringUtils.isEmpty(imgPath) && dx < 0.5 && dy < 0.5 && !isLookMode) {
                    int width = SizeUtils.dp2px(20);
                    if (getRight() - getLeft() - upX < width && upY < width) {
                        if (onDeleteClickListener == null || !onDeleteClickListener.onClick()) {
                            clearImage();
                            invalidate();
                        }
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //清除图片
    public void clearImage() {
        imgPath = null;
        if (holderDrawable != null) {
            setImageDrawable(holderDrawable);
        } else {
            setImageResource(0);
        }
    }

    public SuperImageView setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    //相机类参数
    public SuperImageView setSupportCache(boolean supportCache) {
        this.supportCache = supportCache;
        return this;
    }


    public SuperImageView setSupporCrop(boolean supporCrop) {
        this.supporCrop = supporCrop;
        return this;
    }

    public SuperImageView setOnlyCamera(boolean onlyCamera) {
        isOnlyCamera = onlyCamera;
        return this;
    }

    public SuperImageView setConverType(@Configuration.CoverType int converType) {
        this.converType = converType;
        return this;
    }

    public SuperImageView setMediaQuality(@Configuration.MediaQuality int mediaQuality) {
        this.mediaQuality = mediaQuality;
        return this;
    }

    public SuperImageView setImageHolder(int imageHolder) {
        this.imageHolder = imageHolder;
        setImageResource(imageHolder);
        holderDrawable = getDrawable();
        return this;
    }

    public SuperImageView setLookMode(boolean isLookMode) {
        this.isLookMode = isLookMode;
        return this;
    }

    public SuperImageView setSupportBigImage(boolean supportBigImage) {
        this.supportBigImage = supportBigImage;
        return this;
    }

    public SuperImageView setShape(int shape) {
        this.shape = shape;
        return this;
    }

    public SuperImageView setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
        return this;
    }

    @Override
    public String getValue() {
        return imgPath;
    }

    @Override
    public void setVaule(Object value) {
        show(value);
    }

    public interface OnImageViewClickListener {
        boolean onClick(boolean hasDrawable);
    }

    private OnImageViewClickListener onImageViewClickListener;

    public SuperImageView setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
        this.onImageViewClickListener = onImageViewClickListener;
        return this;
    }

    public interface OnDeleteClickListener {
        boolean onClick();
    }

    private OnDeleteClickListener onDeleteClickListener;

    public SuperImageView setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
        return this;
    }
}
