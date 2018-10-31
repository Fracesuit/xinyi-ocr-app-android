package com.camera.picturelib;

import android.content.Context;
import android.hardware.Camera;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * Created by zhiren.zhang on 2018/10/31.
 */

public class Cameras {

    //
    /**
     * 设置相机预览方向
     * @param context
     * @param camera
     */
    public static void followScreenOrientation(Context context, Camera camera) {

        if (ScreenUtils.isLandscape()) {//横频
            camera.setDisplayOrientation(180);
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


}
