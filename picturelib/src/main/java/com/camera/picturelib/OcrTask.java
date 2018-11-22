package com.camera.picturelib;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.kernal.passport.sdk.utils.Devcode;
import com.xinyi_tech.comm.CommCallBackListener;

import java.util.Arrays;

import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;

/**
 * Created by zhiren.zhang on 2018/11/1.
 */

public class OcrTask extends AsyncTask<String, Integer, String> {
    AppCompatActivity activity;
    ProgressDialog dialog;
    CommCallBackListener<String> commCallBackListener;
    RecogService.recogBinder recogBinder;

    public OcrTask(AppCompatActivity activity, CommCallBackListener<String> commCallBackListener) {
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
        final RecogParameterMessage rpm = new RecogParameterMessage();
        rpm.nMainID = 2;
        rpm.isSaveCut = true;
        rpm.devcode = Devcode.devcode;
        rpm.lpFileName = voids[0];
        rpm.cutSavePath = voids[0];
        String[] GetRecogResult = new String[0];
        try {
            ResultMessage resultMessage = recogBinder.getRecogResult(rpm);
            GetRecogResult = resultMessage.GetRecogResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.toString(GetRecogResult);
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
            recogBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.recogBinder) service;
        }

    };
}
