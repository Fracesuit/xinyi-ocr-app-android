package com.camera.picturelib;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.camera.internal.utils.SDCardUtils2;
import com.xinyi_tech.comm.CommCallBackListener;
import com.xinyi_tech.comm.log.XinYiLog;

import java.io.File;

public class CamerasActivity extends AppCompatActivity {


    LiveCameraView liveCameraView;
    View sbtn_takephoto;
    private File outputPath;

    private Camera camera;
    OcrTask ocrTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);
        super.onCreate(savedInstanceState);
        camera = openCamera();
        if (camera == null) return;

        ocrTask = new OcrTask(this, new CommCallBackListener<String>() {
            @Override
            public void callBack(String s) {
                XinYiLog.e("ds" + s);
                Intent intent = new Intent();
                intent.putExtra("path", outputPath.getPath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setContentView(R.layout.activity_camers);
        initView();
        initData();
        initListener();


    }


    private void initData() {


        Log.d("LiveCameraView", "initData");
        liveCameraView.setCamera(camera);
    }

    private void initListener() {
        sbtn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveCameraView.getCamera().takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        CamerasActivity.this.onPictureTaken(bytes);
                    }
                });
            }
        });
    }

    private void initView() {
        liveCameraView = findViewById(R.id.live_camera_view);
        sbtn_takephoto = findViewById(R.id.sbtn_takephoto);
    }


    private Camera openCamera() {
        int cameraFace = getIntent().getIntExtra("cameraFace", 0);//摄像头类型
        //打开后置摄像头
        Camera camera = Cameras.openCamera(cameraFace);
        if (camera != null) {
            // Cameras.followScreenOrientation(camera);//设置摄像头角度
        } else {
            ToastUtils.showShort("没有摄像机设备或者摄像机被其他程序占用");
        }
        return camera;
    }

    protected void onPictureTaken(final byte[] bytes) {

        //下一步  优化摄像头


        outputPath = new File(SDCardUtils2.getExternalPublic(Environment.DIRECTORY_PICTURES), TimeUtils.getNowMills() + ".jpg");
        //保存图片
         FileIOUtils.writeFileFromBytesByStream(outputPath, bytes, true);


     /*   try {
            final ExifInterface exif = new ExifInterface(outputPath.getAbsolutePath());
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_270 + "");//图片旋转90度，不然看到的就是相差90度的照片,这个xu
            exif.saveAttributes();
        } catch (Throwable error) {
            Log.e("CamerasActivity", "Can't save exif info: " + error.getMessage());
        }*/
      /*  int ocrType = getIntent().getIntExtra("ocrType", 2);//证件识别类型  默认识别身份证
        Bitmap bitmap = ImageUtils.getBitmap(bytes, 0);
        Bitmap rotate = ImageUtils.rotate(bitmap, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true);//摄像头转多少度，这个图片就要转多少度

        ImageUtils.save(rotate, outputPath, Bitmap.CompressFormat.JPEG);
        rotate.recycle();*/
        ocrTask.execute(outputPath.getPath());
        //继续拍照或者拍照之后返回或者跳转到预览页面
        //liveCameraView.getCamera().startPreview();
        XinYiLog.e(Thread.currentThread().getName());

    }


    @Override
    protected void onDestroy() {
        if (camera != null) {
            //释放相机资源
            camera.release();
        }

        super.onDestroy();
    }


}
