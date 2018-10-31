package com.camera.internal.controller.view;

import android.view.View;

import com.camera.configuration.Configuration;
import com.camera.internal.utils.Size;

/*
 * Created by memfis on 7/6/16.
 */
public interface CameraView {

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@Configuration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken(byte[] bytes);

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop();

    void releaseCameraPreview();

}
