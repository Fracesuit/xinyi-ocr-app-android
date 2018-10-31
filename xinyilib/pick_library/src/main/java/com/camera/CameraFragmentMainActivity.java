package com.camera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.camera.configuration.Configuration;
import com.camera.listeners.CameraFragmentControlsAdapter;
import com.camera.listeners.CameraFragmentResultListener;
import com.camera.listeners.CameraFragmentStateAdapter;
import com.camera.listeners.CameraFragmentVideoRecordTextAdapter;
import com.camera.widgets.CameraSettingsView;
import com.camera.widgets.CameraSwitchView;
import com.camera.widgets.FlashSwitchView;
import com.camera.widgets.RecordButton;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureSelectionConfig;

import java.io.File;


public class CameraFragmentMainActivity extends AppCompatActivity {

    public static final String FRAGMENT_TAG = "camera";
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int REQUEST_PREVIEW_CODE = 1001;

    CameraSettingsView settingsView;
    FlashSwitchView flashSwitchView;
    CameraSwitchView cameraSwitchView;
    RecordButton recordButton;

    TextView recordDurationText;
    TextView recordSizeText;
    PictureSelectionConfig model;
    private boolean mFaceDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getIntent().getParcelableExtra("model");
        //设置横竖屏
      /*  if (model.customCameraScreenPortait != ScreenUtils.isPortrait()) {
            ScreenUtils.setLandscape(this);
        }*/
        setContentView(ScreenUtils.isLandscape() ? R.layout.camerafragment_activity_main_land : R.layout.camerafragment_activity_main_port);
        mFaceDetection = Configuration.COVERTYPE_SMART_FACE == model.customCameraCoverType;
        initView();
        startCamera();
    }

    private void initView() {
        settingsView = (CameraSettingsView) findViewById(R.id.settings_view);
        settingsView.setVisibility(model.customCameraSupportSetting ? View.VISIBLE : View.GONE);
        flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
        cameraSwitchView.setVisibility((model.customCameraSupportSwitchFace && Configuration.COVERTYPE_RECTANGLE != model.customCameraCoverType) ? View.VISIBLE : View.GONE);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setVisibility(!mFaceDetection ? View.VISIBLE : View.GONE);
        recordDurationText = (TextView) findViewById(R.id.record_duration_text);
        recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);


        initListener();


    }

    private void initListener() {
        flashSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.toggleFlashMode();
                }
            }
        });
        cameraSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.switchCameraTypeFrontBack();
                }
            }
        });
        recordButton.setRecordButtonListener(new RecordButton.RecordButtonListener() {
            @Override
            public void onRecordButtonClicked() {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.takePhotoOrCaptureVideo();
                }
            }
        });
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.openSettingDialog();
                }
            }
        });


    }


    public void startCamera() {
        @SuppressLint("MissingPermission") final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder(model.customCameraMediaPath,
                model.customCameraMediaName)
                .setCamera(model.customCameraCameraFace)
                .setMediaAction(model.customCameraMediaAction)
                .setMediaQuality(model.customCameraMediaQuality)
                .setFaceDetection(mFaceDetection)
                .setMessage(model.customCameraMessage)
                .setVideoDuration(model.videoMinSecond == 0 ? -1 : model.videoMinSecond * 1000)
                .setCoverType(model.customCameraCoverType)
                .setFlashMode(Configuration.FLASH_MODE_OFF)
                .build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();


        if (cameraFragment != null) {
            cameraFragment.setResultListener(new CameraFragmentResultListener() {
                @Override
                public void onVideoRecorded(String filePath) {
                    if (model.customCameraSupportPreview) {
                        Intent intent = PreviewActivity.newIntentVideo(CameraFragmentMainActivity.this, filePath);
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                    } else {
                        finish();
                    }

                }

                @Override
                public void onPhotoTaken(byte[] bytes, String filePath) {
                    if (model.customCameraSupportPreview) {
                        Intent intent = PreviewActivity.newIntentPhoto(CameraFragmentMainActivity.this, filePath);
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                    } else {
                        finish();
                    }
                }
            });

            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitchView.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitchView.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    flashSwitchView.displayFlashAuto();
                }

                @Override
                public void onFlashOn() {
                    flashSwitchView.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    flashSwitchView.displayFlashOff();
                }

                @Override
                public void onCameraSetupForPhoto() {

                    recordButton.displayPhotoState();
                }

                @Override
                public void onCameraSetupForVideo() {
                    recordButton.displayVideoRecordStateReady();
                }

                @Override
                public void shouldRotateControls(int degrees) {
                    //ViewCompat.setRotation(cameraSwitchView, degrees);
                    // ViewCompat.setRotation(mediaActionSwitchView, degrees);
                    //ViewCompat.setRotation(flashSwitchView, degrees);
                    //ViewCompat.setRotation(recordDurationText, degrees);
                    // ViewCompat.setRotation(recordSizeText, degrees);
                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                    recordButton.displayVideoRecordStateReady();
                }

                @Override
                public void onRecordStateVideoInProgress() {
                    recordButton.displayVideoRecordStateInProgress();
                }

                @Override
                public void onRecordStatePhoto() {
                    recordButton.displayPhotoState();
                }

                @Override
                public void onStopVideoRecord() {
                    recordSizeText.setVisibility(View.GONE);
                    //cameraSwitchView.setVisibility(View.VISIBLE);
                    //settingsView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                }
            });

            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                    cameraSwitchView.setEnabled(false);
                    recordButton.setEnabled(false);
                    settingsView.setEnabled(false);
                    flashSwitchView.setEnabled(false);
                }

                @Override
                public void unLockControls() {
                    cameraSwitchView.setEnabled(true);
                    recordButton.setEnabled(true);
                    settingsView.setEnabled(true);
                    flashSwitchView.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    //cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    recordButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                }
            });

            cameraFragment.setTextListener(new CameraFragmentVideoRecordTextAdapter() {
                @Override
                public void setRecordSizeText(long size, String text) {
                    recordSizeText.setText(text);
                }

                @Override
                public void setRecordSizeTextVisible(boolean visible) {
                    recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                @Override
                public void setRecordDurationText(String text) {
                    recordDurationText.setText(text);
                }

                @Override
                public void setRecordDurationTextVisible(boolean visible) {
                    recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }


    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PREVIEW_CODE) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
