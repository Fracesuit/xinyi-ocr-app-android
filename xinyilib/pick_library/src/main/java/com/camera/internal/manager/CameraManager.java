package com.camera.internal.manager;

import android.content.Context;

import com.camera.configuration.Configuration;
import com.camera.configuration.ConfigurationProvider;
import com.camera.internal.manager.listener.CameraCloseListener;
import com.camera.internal.manager.listener.CameraOpenListener;
import com.camera.internal.manager.listener.CameraPhotoListener;
import com.camera.internal.manager.listener.CameraVideoListener;
import com.camera.internal.utils.Size;

import java.io.File;

/*
 * Created by memfis on 8/14/16.
 */
public interface CameraManager<CameraId, SurfaceListener> {

    void initializeCameraManager(ConfigurationProvider configurationProvider, Context context);

    void openCamera(File outputPath, CameraId cameraId, CameraOpenListener<CameraId, SurfaceListener> cameraOpenListener);

    void closeCamera(CameraCloseListener<CameraId> cameraCloseListener);

    void setFlashMode(@Configuration.FlashMode int flashMode);

    void takePhoto(CameraPhotoListener cameraPhotoListener);

    void startVideoRecord(CameraVideoListener cameraVideoListener);

    Size getPhotoSizeForQuality(@Configuration.MediaQuality int mediaQuality);

    void stopVideoRecord();

    void releaseCameraManager();

    CameraId getCurrentCameraId();

    CameraId getFaceFrontCameraId();

    CameraId getFaceBackCameraId();

    int getNumberOfCameras();

    int getFaceFrontCameraOrientation();

    int getFaceBackCameraOrientation();

    boolean isVideoRecording();

    CharSequence[] getVideoQualityOptions();

    CharSequence[] getPhotoQualityOptions();

    void setCameraId(CameraId currentCameraId);
}
