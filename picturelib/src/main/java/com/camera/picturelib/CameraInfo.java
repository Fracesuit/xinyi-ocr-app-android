package com.camera.picturelib;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhiren.zhang on 2018/11/1.
 */

public class CameraInfo {
    public static final int CAMERA_FACE_FRONT = 1;
    public static final int CAMERA_FACE_REAR = 0;

    @IntDef({CAMERA_FACE_FRONT, CAMERA_FACE_REAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraFace {
    }
}
