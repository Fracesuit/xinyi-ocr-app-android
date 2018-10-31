package com.camera.picturelib;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.camera.configuration.Configuration;
import com.camera.internal.manager.impl.Camera1Manager;
import com.camera.widgets.cover.SuperCoverView;
import com.kernal.passport.sdk.utils.Devcode;
import com.kernal.passportreader.sdk.CameraActivity;
import com.xinyi_tech.comm.log.XinYiLog;
import com.xinyi_tech.comm.permission.DefaultRequestPermissionsListener;
import com.xinyi_tech.comm.permission.PermissionsHelp;
import com.xinyi_tech.comm.util.FileUtils2;
import com.xinyi_tech.comm.util.ToastyUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;

public class CamersActivity extends AppCompatActivity {


    LiveCameraView liveCameraView;
    View sbtn_takephoto;
    private File outputPath;
    RecogService.recogBinder recogBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//  hiding titles
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camers);
        liveCameraView = findViewById(R.id.live_camera_view);
        sbtn_takephoto = findViewById(R.id.sbtn_takephoto);
        sbtn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveCameraView.getmCamera().takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        CamersActivity.this.onPictureTaken(bytes);
                    }
                });
            }
        });
        Intent recogIntent = new Intent(CamersActivity.this, RecogService.class);
        bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
        //权限
        PermissionsHelp.with(this)
                .requestPermissions(new DefaultRequestPermissionsListener() {
                    @Override
                    public void grant() {

                        openCamera();
                    }
                }, PermissionsHelp.CAMERA, PermissionsHelp.WRITE_EXTERNAL_STORAGE);
    }

    private void openCamera() {
        int cameras = Camera.getNumberOfCameras();
        if (cameras > 0) {
            Camera camera = Cameras.openCamera(0);//打开后置摄像头
            if (camera != null) {
                liveCameraView.setCamera(camera);
            } else {
                ToastUtils.showShort("没有摄像机设备或者摄像机被其他程序占用");
            }

        } else {
            ToastUtils.showShort("没有摄像机设备");
        }
    }
    protected void onPictureTaken(final byte[] bytes) {
        outputPath = new File(FileUtils2.getPhotoUri().getPath());
        //保存图片
        FileIOUtils.writeFileFromBytesByStream(outputPath, bytes, false);

        //继续拍照或者拍照之后返回或者跳转到预览页面
        liveCameraView.getmCamera().startPreview();
       /* try {
            final ExifInterface exif = new ExifInterface(outputPath.getAbsolutePath());
         //   exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + getPhotoOrientation(configurationProvider.getSensorPosition()));
            exif.saveAttributes();
        } catch (Throwable error) {
            Log.e("CamersActivity", "Can't save exif info: " + error.getMessage());
        }*/

          RecogParameterMessage rpm = new RecogParameterMessage();
         rpm.nTypeLoadImageToMemory = 0;
         rpm.nMainID = 2;
         rpm.nSubID = null;
         rpm.GetSubID = true;
         rpm.GetVersionInfo = true;
         rpm.logo = "";
         rpm.userdata = "";
         rpm.sn = "";
         rpm.authfile = "";
         rpm.isSaveCut = true;
         rpm.triggertype = 0;
         rpm.devcode = Devcode.devcode;
         rpm.isOnlyClassIDCard = true;
         //rpm.isSetIDCardRejectType=false;
         // rpm.idcardRotateDegree=3;
         rpm.isAutoClassify = true;
         rpm.nv21bytes = bytes;
         rpm.nv21_width = 2560;
         rpm.nv21_height = 1440;
         rpm.lpHeadFileName = "";//  save document protrait
         rpm.lpFileName = outputPath.getPath(); //  If rpm.lpFileName is null, automatic recognition fuction will be executed
         rpm.cutSavePath = outputPath.getPath();
        try {
            ResultMessage resultMessage = recogBinder.getRecogResult(rpm);
            String[] GetRecogResult = resultMessage.GetRecogResult;
            XinYiLog.e(GetRecogResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            recogBinder = (RecogService.recogBinder) service;

        }

    };

    @Override
    protected void onDestroy() {
        unbindService(recogConn);
        super.onDestroy();
    }
}
