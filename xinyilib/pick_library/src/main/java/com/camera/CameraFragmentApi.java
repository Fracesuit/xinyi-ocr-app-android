package com.camera;

import com.camera.internal.ui.model.PhotoQualityOption;
import com.camera.listeners.CameraFragmentControlsListener;
import com.camera.listeners.CameraFragmentResultListener;
import com.camera.listeners.CameraFragmentStateListener;
import com.camera.listeners.CameraFragmentVideoRecordTextListener;

/*
 * Created by florentchampigny on 16/01/2017.
 */

public interface CameraFragmentApi {

    void takePhotoOrCaptureVideo();

    void openSettingDialog();

    PhotoQualityOption[] getPhotoQualities();

    void switchCameraTypeFrontBack();

    void switchActionPhotoVideo();

    void toggleFlashMode();

    void setStateListener(CameraFragmentStateListener cameraFragmentStateListener);

    void setTextListener(CameraFragmentVideoRecordTextListener cameraFragmentVideoRecordTextListener);

    void setControlsListener(CameraFragmentControlsListener cameraFragmentControlsListener);

    void setResultListener(CameraFragmentResultListener cameraFragmentResultListener);

}
