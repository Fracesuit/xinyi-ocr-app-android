package com.camera.picturelib;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.ScreenUtils;
import com.camera.configuration.Configuration;
import com.camera.internal.utils.CameraHelper;
import com.camera.internal.utils.Size;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/10/31.
 */

public class LiveCameraView extends SurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = LiveCameraView.class.getSimpleName();
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;

    public LiveCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Start preview display[SURFACE-CREATED]");
        startPreviewDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        setCameraParams(width, height);

        stopPreviewDisplay();
        startPreviewDisplay(mSurfaceHolder);
    }

    private void setCameraParams(int width, int height) {
        Cameras.followScreenOrientation(mCamera);//设置相机角度，不然预览的图像和真实的图像会成90度

        Camera.Parameters parameters = mCamera.getParameters();

        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setExposureCompensation(0);

        final List<Size> previewSizes = Size.fromList(parameters.getSupportedPreviewSizes());
        final List<Size> pictureSizes = Size.fromList(parameters.getSupportedPictureSizes());

        Size optimalPreviewSize = Cameras.getOptimalPreviewSize(previewSizes, width, height);
        parameters.setPreviewSize(optimalPreviewSize.getWidth(), optimalPreviewSize.getHeight());

        Size optimalPictureSize = Cameras.getOptimalPictureSize(pictureSizes, width, height);
        parameters.setPictureSize(optimalPictureSize.getWidth(), optimalPictureSize.getHeight());

        mCamera.setParameters(parameters);
    }


    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    public Camera getCamera() {
        return mCamera;
    }

    private void startPreviewDisplay(SurfaceHolder holder) {
        checkCamera();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error while START preview for camera", e);
        }
    }

    private void stopPreviewDisplay() {
        checkCamera();
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error while STOP preview for camera", e);
        }
    }

    private void checkCamera() {
        if (mCamera == null) {
            throw new IllegalStateException("Camera must be set when start/stop preview, call <setCamera(Camera)> to set");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Stop preview display[SURFACE-DESTROYED]");
        stopPreviewDisplay();
    }
}

