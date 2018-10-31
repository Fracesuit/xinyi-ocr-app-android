package com.camera.internal.manager.listener;

import com.camera.internal.utils.Size;

import java.io.File;


/*
 * Created by memfis on 8/14/16.
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
