package com.camera.picturelib;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ImageUtils;
import com.kernal.plateid.Devcode;
import com.kernal.plateid.PlateCfgParameter;
import com.kernal.plateid.PlateRecognitionParameter;
import com.xinyi_tech.comm.CommCallBackListener;

import com.kernal.plateid.RecogService;
import com.xinyi_tech.comm.log.XinYiLog;

import java.util.Arrays;


/**
 * Created by zhiren.zhang on 2018/11/1.
 */

public class OcrTask2 extends AsyncTask<String, Integer, String> {
    AppCompatActivity activity;
    ProgressDialog dialog;
    CommCallBackListener<String> commCallBackListener;
    public RecogService.MyBinder recogBinder;

    public OcrTask2(AppCompatActivity activity, CommCallBackListener<String> commCallBackListener) {
        this.commCallBackListener = commCallBackListener;
        this.activity = activity;
        Intent recogIntent = new Intent(activity, RecogService.class);
        activity.bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setTitle("识别");
        dialog.setMessage("请稍等,正在识别中...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.show();


    }

    @Override
    protected String doInBackground(String... voids) {
       /* PlateRecognitionParameter prp = new PlateRecognitionParameter();
        ;
        // 开发码
        prp.devCode = com.kernal.plateid.Devcode.DEVCODE;
        //  prp.width=1080;
        //  prp.height=1920;
        Bitmap bitmap = ImageUtils.getBitmap("/storage/emulated/0/DCIM/Camera/plateID_20181101_165047_2.jpg");

        prp.picByte = ImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG);
        String[] fieldvalue = recogBinder.doRecogDetail(prp);
        int nRet = recogBinder.getnRet();
     ;*/

        PlateRecognitionParameter prp = new PlateRecognitionParameter();
        prp.height = 1920;// 图像高度
        prp.width = 1080;// 图像宽度
        prp.pic = voids[0];//"/storage/emulated/0/DCIM/Camera/plateID_20181101_165047.jpg";// 图像文件
        // prp.dataFile = dataFile;
        // prp.isCheckDevType =
        // true;//检查设备型号授权所用参数,另一个参数为devCode
        prp.devCode = Devcode.DEVCODE;
        // prp.versionfile =
        // Environment.getExternalStorageDirectory().toString()+"/AndroidWT/wtversion.lsc";;
        // System.out.println("图像宽高"+height+"    "+width);
        String[] fieldvalue = recogBinder.doRecogDetail(prp);

        XinYiLog.e(Arrays.toString(fieldvalue));
        String number = fieldvalue[0];
        String color = fieldvalue[1];
        return number + " " + color;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        activity.unbindService(recogConn);
        dialog.dismiss();
        commCallBackListener.callBack(aVoid);
    }

    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // System.out.println("ResultActivity onServiceConnected");
            recogBinder = (RecogService.MyBinder) service;
            int iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();

            // recogBinder.setRecogArgu(recogPicPath,
            // imageformat,
            // bGetVersion, bVertFlip, bDwordAligned);
            PlateCfgParameter cfgparameter = new PlateCfgParameter();
            cfgparameter.armpolice = 4;// 单层武警车牌是否开启:4是；5不是
            cfgparameter.armpolice2 = 16;// 双层武警车牌是否开启:16是；17不是
            cfgparameter.embassy = 12;// 使馆车牌是否开启:12是；13不是
            cfgparameter.individual = 0;// 是否开启个性化车牌:0是；1不是
            // cfgparameter.nContrast = 9;//
            // 清晰度指数(取值范围0-9,最模糊时设为1;最清晰时设为9)
            cfgparameter.nOCR_Th = 0;
            cfgparameter.nPlateLocate_Th = 5;// 识别阈值(取值范围0-9,5:默认阈值0:最宽松的阈值9:最严格的阈值)
            cfgparameter.onlylocation = 15;// 只定位车牌是否开启:14是；15不是
            cfgparameter.tworowyellow = 2;// 双层黄色车牌是否开启:2是；3不是
            cfgparameter.tworowarmy = 6;// 双层军队车牌是否开启:6是；7不是
            cfgparameter.szProvince = "";// 省份顺序
            cfgparameter.onlytworowyellow = 11;// 只识别双层黄牌是否开启:10是；11不是
            cfgparameter.tractor = 8;// 农用车车牌是否开启:8是；9不是
            cfgparameter.bIsNight = 1;// 是否夜间模式：1是；0不是
            cfgparameter.newEnergy = 24; //新能源车牌开启
            cfgparameter.consulate = 22;  //领事馆车牌开启
            // //废弃参数
            recogBinder.setRecogArgu(cfgparameter, 0, 0,
                    1);

            // fieldvalue =
            // recogBinder.doRecog(recogPicPath, width,
            // height);

               /* nRet = recogBinder.getnRet();
                if (nRet != 0) {
                    String[] str = { "" + nRet };
                    getResult(str);
                } else {
                    getResult(fieldvalue);
                }
*/


        }
    };
}
