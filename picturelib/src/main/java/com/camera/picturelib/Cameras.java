package com.camera.picturelib;

import android.content.Context;
import android.hardware.Camera;

import com.blankj.utilcode.util.ScreenUtils;
import com.camera.configuration.Configuration;
import com.camera.internal.utils.CameraHelper;
import com.camera.internal.utils.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhiren.zhang on 2018/10/31.
 */

public class Cameras {

    //
    /**
     * 设置相机预览方向
     * @param camera
     */
    public static void followScreenOrientation( Camera camera) {

        if (ScreenUtils.isLandscape()) {//横频
            camera.setDisplayOrientation(0);
        } else if (ScreenUtils.isPortrait()) {
            camera.setDisplayOrientation(90);
        }
    }

    public static Camera openCamera(int cameraId) {
        try{
            return Camera.open(cameraId);
        }catch(Exception e) {
            return null;
        }
    }
    public static Size getOptimalPreviewSize(List<Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;

        double targetRatio = ScreenUtils.isPortrait() ? (double) height / width : (double) width / height;

        if (sizes == null) return null;
        Collections.sort(sizes, new Cameras.CompareSizesByArea1());

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = ScreenUtils.isPortrait() ? width : height;

        for (Size size : sizes) {
            double ratio = (double) size.getWidth() / size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {

            minDiff = Double.MAX_VALUE;
            int index = 0;
            for (int i = index, len = sizes.size(); i < len; i++) {
                Size size = sizes.get(i);
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    public static Size getOptimalPictureSize(List<Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;

        double targetRatio = ScreenUtils.isPortrait() ? (double) height / width : (double) width / height;

        if (sizes == null) return null;
        Collections.sort(sizes, new Cameras.CompareSizesByArea1());

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = getPictureSize(sizes).getHeight();

        for (Size size : sizes) {
            double ratio = (double) size.getWidth() / size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {

            minDiff = Double.MAX_VALUE;
            int index = 0;
            for (int i = index, len = sizes.size(); i < len; i++) {
                Size size = sizes.get(i);
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @SuppressWarnings("deprecation")
    public static Size getPictureSize(List<Size> choices) {
        if (choices == null || choices.isEmpty()) return null;
        if (choices.size() == 1) return choices.get(0);

        return Collections.min(choices, new Cameras.CompareSizesByArea2());
    }
    private static class CompareSizesByArea1 implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) rhs.getWidth() * rhs.getHeight() - (long) lhs.getWidth() * lhs.getHeight()
            );
        }

    }

    private static class CompareSizesByArea2 implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
