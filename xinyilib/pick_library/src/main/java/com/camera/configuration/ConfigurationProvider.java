package com.camera.configuration;


/*
 * Created by memfis on 7/6/16.
 */
public interface ConfigurationProvider {

    @Configuration.MediaAction
    int getMediaAction();

    @Configuration.MediaQuality
    int getMediaQuality();

    int getVideoDuration();

    long getVideoFileSize();

    @Configuration.SensorPosition
    int getSensorPosition();

    int getDegrees();

    int getMinimumVideoDuration();

    @Configuration.FlashMode
    int getFlashMode();

    @Configuration.CameraFace
    int getCameraFace();

    @Configuration.CoverType
    int getCoverType();

    void setMediaQuality(int mediaQuality);

    void setPassedMediaQuality(int mediaQuality);

    void setVideoDuration(int videoDuration);

    void setVideoFileSize(long videoFileSize);

    void setMinimumVideoDuration(int minimumVideoDuration);

    void setFlashMode(int flashMode);

    void setSensorPosition(int sensorPosition);

    int getDeviceDefaultOrientation();

    void setDegrees(int degrees);

    void setMediaAction(int mediaAction);

    void setDeviceDefaultOrientation(int deviceDefaultOrientation);

    int getPassedMediaQuality();

    String getMessage();

    void setupWithAnnaConfiguration(Configuration configuration);

    String getMediaPath();

    String getMediaName();

    boolean faceDetection();

}
