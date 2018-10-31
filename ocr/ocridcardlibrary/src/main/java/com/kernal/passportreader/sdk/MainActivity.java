package com.kernal.passportreader.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.kernal.lisence.DeviceFP;
import com.kernal.passport.sdk.utils.ActivityRecogUtils;
import com.kernal.passport.sdk.utils.PermissionActivity;
import com.kernal.passport.sdk.utils.AppManager;
import com.kernal.passport.sdk.utils.CheckPermission;
import com.kernal.passport.sdk.utils.Devcode;
import com.kernal.passport.sdk.utils.SharedPreferencesHelper;
import com.kernal.passportreader.sdk.R;

import android.Manifest;
import android.hardware.usb.UsbInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import kernal.idcard.android.AuthParameterMessage;
import kernal.idcard.android.AuthService;
import kernal.idcard.android.IDCardAPI;
import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;

public class MainActivity extends Activity implements OnClickListener {
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int srcWidth, srcHeight;
    private Button btn_chooserIdCardType, btn_takePicture, btn_exit,
            btn_importRecog, btn_ActivationProgram,btn_Intelligent_detecting_edges;
    private boolean isQuit = false;
    private Timer timer = new Timer();
    private String[][] type2 = {{"机读码", "3000"}, {"护照", "13"},
            {"居民身份证", "2"}, {"港澳通行证", "9"}, {"大陆居民往来台湾通行证", "11"},
            {"签证", "12"}, {"新版港澳通行证", "22"}, {"中国驾照", "5"},
            {"中国行驶证", "6"}, {"香港身份证", "1001"}, {"回乡证(正面)", "14"},
            {"回乡证(背面)", "15"}, {"澳门身份证", "1005"}, {"新版澳门身份证", "1012"},
            {"台胞证", "10"}, {"新版台胞证(正面)", "25"}, {"新版台胞证(背面)", "26"},
            {"台湾身份证(正面)", "1031"}, {"台湾身份证(背面)", "1032"},{"中国军官证", "7"},
            {"全民健康保险卡", "1030"}, {"马来西亚身份证", "2001"}, {"新加坡身份证", "2004"},
            {"新西兰驾照", "2003"}, {"加利福尼亚驾照", "2002"}, {"印度尼西亚身份证", "2010"}, {"泰国身份证", "2011"}};
    private int nMainID = 0;
    public static int DIALOG_ID = -1;
    private String[] type;
    public RecogService.recogBinder recogBinder;
    private String recogResultString = "";
    private String selectPath = "";
    private int nMainId = 2;
    private String[] recogTypes = {"机读码(2*44)", "机读码(2*36)", "机读码(3*30)"};
    private String[] IDCardTypes = {"身份证（正面）", "身份证（背面）"};
    private AuthService.authBinder authBinder;
    private int ReturnAuthority = -1;
    private String sn = "";
    private AlertDialog dialog;
    private EditText editText;
    private String devcode = Devcode.devcode;//project licensing development code
    public ServiceConnection authConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            authBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            authBinder = (AuthService.authBinder) service;
            try {

                AuthParameterMessage apm = new AuthParameterMessage();
                // apm.datefile = "assets"; // PATH+"/IDCard/wtdate.lsc";// Reservation
                apm.devcode = devcode;//  Reservation
                apm.sn = sn;
                ReturnAuthority = authBinder.getIDCardAuth(apm);
                if (ReturnAuthority != 0) {
                    Toast.makeText(getApplicationContext(),
                            "ReturnAuthority:" + ReturnAuthority,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.activation_success),
                            Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        getString(R.string.license_verification_failed),
                        Toast.LENGTH_LONG).show();

            } finally {
                if (authBinder != null) {
                    unbindService(authConn);
                }
            }
        }
    };
    public static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//  Write access
            Manifest.permission.READ_EXTERNAL_STORAGE, //  read access
            Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.VIBRATE, Manifest.permission.INTERNET,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//  hiding titles
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//  setting up the full screen
        //  Screen always on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        srcWidth = displayMetrics.widthPixels;
        srcHeight = displayMetrics.heightPixels;
        setContentView(getResources().getIdentifier("activity_main", "layout",
                getPackageName()));
        findView();
        //  the came interface being released
        AppManager.getAppManager().finishAllActivity();

    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        type2 = intiList();
        type = new String[type2.length];

        for (int i = 0; i < type2.length; i++) {
            type[i] = type2[i][0];
        }
        if (getResources().getConfiguration().locale.getLanguage().equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("CN")) {
            RecogService.nTypeInitIDCard = 3;
        } else if (getResources().getConfiguration().locale.getLanguage()
                .equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("TW")) {
            RecogService.nTypeInitIDCard = 3;
        } else {
            RecogService.nTypeInitIDCard = 4;
        }
    }

    /**
     * @Title: findView @Description: TODO(这里用一句话描述这个方法的作用) @param 设定文件 @return
     * void 返回类型 @throws
     */
    private void findView() {
        // TODO Auto-generated method stub

        btn_chooserIdCardType = (Button) findViewById(getResources().getIdentifier("btn_chooserIdCardType", "id",
                getPackageName()));
        btn_takePicture = (Button) findViewById(getResources().getIdentifier("btn_takePicture", "id",
                getPackageName()));
        btn_exit = (Button) findViewById(getResources().getIdentifier("btn_exit", "id",
                getPackageName()));
        btn_importRecog = (Button) findViewById(getResources().getIdentifier("btn_importRecog", "id",
                getPackageName()));
        btn_ActivationProgram = (Button) findViewById(getResources().getIdentifier("btn_ActivationProgram", "id",
                getPackageName()));
        btn_Intelligent_detecting_edges= (Button) findViewById(getResources().getIdentifier("btn_Intelligent_detecting_edges", "id",
                getPackageName()));
        btn_ActivationProgram.setOnClickListener(this);
        btn_chooserIdCardType.setOnClickListener(this);
        btn_takePicture.setOnClickListener(this);
        btn_Intelligent_detecting_edges.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_importRecog.setOnClickListener(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                srcWidth / 2, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) (srcHeight * 0.25);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_ActivationProgram.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams(srcWidth / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.btn_ActivationProgram);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_chooserIdCardType.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams(srcWidth / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.btn_chooserIdCardType);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_takePicture.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams(srcWidth / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.btn_takePicture);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_Intelligent_detecting_edges.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams(srcWidth / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.btn_Intelligent_detecting_edges);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_importRecog.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams(srcWidth / 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.btn_importRecog);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_exit.setLayoutParams(params);

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        if (getResources()
                .getIdentifier("btn_ActivationProgram", "id", this.getPackageName()) == v.getId()) {
            if (Build.VERSION.SDK_INT >= 23) {
                CheckPermission checkPermission = new CheckPermission(this);
                if (checkPermission.permissionSet(PERMISSION)) {
                    PermissionActivity.startActivityForResult(this, 0,
                            SharedPreferencesHelper.getInt(
                                    getApplicationContext(), "nMainId", 2),
                            devcode, 0, 0,0, PERMISSION);
                } else {
                    activationProgramOpera();
                }
            } else {
                activationProgramOpera();
            }

        } else if (getResources()
                .getIdentifier("btn_chooserIdCardType", "id", this.getPackageName()) == v.getId()) {

                dialog();


        } else if (getResources()
                .getIdentifier("btn_takePicture", "id", this.getPackageName()) == v.getId()) {
/**
 * It will wast a lot of time if such resources like camera is released in the camera interface.
 * To optimize user experience, it is necessary to invoke AppManager.getAppManager().finishAllActivity() which
 * is stored in oncreate() method in the camera interface.
 * If the displaying interface is the one to invoke and recognize,
 * this interface can only be invoked once.
 * If there are two displaying interfaces,
 * it is necessary to invoke AppManager.getAppManager().finishAllActivity() which
 * is stored in oncreate() method in the displaying interface.
 * Otherwise, it will lead to the overflow of internal memeory.
 */
            intent = new Intent(MainActivity.this, CameraActivity.class);
            if (Build.VERSION.SDK_INT >= 23) {
                CheckPermission checkPermission = new CheckPermission(this);
                if (checkPermission.permissionSet(PERMISSION)) {
                    PermissionActivity.startActivityForResult(this, 0,
                            SharedPreferencesHelper.getInt(
                                    getApplicationContext(), "nMainId", 2),
                            devcode, 0, 0,0, PERMISSION);
                } else {
                    intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                            getApplicationContext(), "nMainId", 2));
                    intent.putExtra("devcode", devcode);
                    intent.putExtra("flag", 0);
                    intent.putExtra("nCropType", 0);
                    MainActivity.this.finish();
                    startActivity(intent);
                }
            } else {
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        getApplicationContext(), "nMainId", 2));
                intent.putExtra("devcode", devcode);
                intent.putExtra("flag", 0);
                intent.putExtra("nCropType", 0);
                MainActivity.this.finish();
                startActivity(intent);
            }
        }else if (getResources()
                .getIdentifier("btn_Intelligent_detecting_edges", "id", this.getPackageName()) == v.getId()) {
/**
 * It will wast a lot of time if such resources like camera is released in the camera interface.
 * To optimize user experience, it is necessary to invoke AppManager.getAppManager().finishAllActivity() which
 * is stored in oncreate() method in the camera interface.
 * If the displaying interface is the one to invoke and recognize,
 * this interface can only be invoked once.
 * If there are two displaying interfaces,
 * it is necessary to invoke AppManager.getAppManager().finishAllActivity() which
 * is stored in oncreate() method in the displaying interface.
 * Otherwise, it will lead to the overflow of internal memeory.
 */
            intent = new Intent(MainActivity.this, CameraActivity.class);
            if (Build.VERSION.SDK_INT >= 23) {
                CheckPermission checkPermission = new CheckPermission(this);
                if (checkPermission.permissionSet(PERMISSION)) {
                    PermissionActivity.startActivityForResult(this, 0,
                            SharedPreferencesHelper.getInt(
                                    getApplicationContext(), "nMainId", 2),
                            devcode, 0, 0,1, PERMISSION);
                } else {
                    intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                            getApplicationContext(), "nMainId", 2));
                    intent.putExtra("devcode", devcode);
                    intent.putExtra("flag", 0);
                    intent.putExtra("nCropType", 1);
                    MainActivity.this.finish();
                    startActivity(intent);
                }
            } else {
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        getApplicationContext(), "nMainId", 2));
                intent.putExtra("devcode", devcode);
                intent.putExtra("flag", 0);
                intent.putExtra("nCropType", 1);
                MainActivity.this.finish();
                startActivity(intent);
            }
        } else if (getResources()
                .getIdentifier("btn_importRecog", "id", this.getPackageName()) == v.getId()) {
// Phote Album
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            try {
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture)),
                        9);
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.install_fillManager), Toast.LENGTH_SHORT).show();
            }
        } else if (getResources()
                .getIdentifier("btn_exit", "id", this.getPackageName()) == v.getId()) {
            MainActivity.this.finish();

        }


    }

    /**
     * @Title: activationProgramOpera @Description: TODO(这里用一句话描述这个方法的作用) @param
     * 设定文件 @return void 返回类型 @throws
     */
    private void activationProgramOpera() {
        // TODO Auto-generated method stub
        DIALOG_ID = 1;
        View view = getLayoutInflater().inflate(R.layout.serialdialog, null);
        editText = (EditText) view.findViewById(R.id.serialdialogEdittext);
        dialog = new Builder(MainActivity.this)
                .setView(view)
                .setPositiveButton(getString(R.string.online_activation),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm.isActive()) {
                                    imm.toggleSoftInput(
                                            InputMethodManager.SHOW_IMPLICIT,
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                String editsString = editText.getText()
                                        .toString().toUpperCase();
                                if (editsString != null) {
                                    sn = editsString;
                                }
                                if (isNetworkConnected(MainActivity.this)) {
                                    if (isWifiConnected(MainActivity.this)
                                            || isMobileConnected(MainActivity.this)) {
                                        startAuthService();
                                        dialog.dismiss();
                                    } else if (!isWifiConnected(MainActivity.this)
                                            && !isMobileConnected(MainActivity.this)) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                getString(R.string.network_unused),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getString(R.string.please_connect_network),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }

                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }

                        }).create();
        dialog.show();
    }

    /**
     * @Title: dialog @Description: TODO(这里用一句话描述这个方法的作用) @param 设定文件 @return
     * void 返回类型 @throws
     */
    private void dialog() {
        // TODO Auto-generated method stub

        int checkedItem = -1;
        /*
         * if
		 * (getResources().getConfiguration().locale.getLanguage().equals("zh")
		 * && getResources().getConfiguration().locale.getCountry()
		 * .equals("CN")) {
		 */
        for (int i = 0; i < type2.length; i++) {

            if (Integer.valueOf(type2[i][1]) == SharedPreferencesHelper.getInt(
                    getApplicationContext(), "nMainId", 2)) {
                checkedItem = i;
                break;
            }
        }

        // }
        // The selected results in the type column do not disapper
        DIALOG_ID = 1;
        Builder dialog = createAlertDialog(
                getString(R.string.chooseRecogType), null);
        dialog.setPositiveButton(getString(R.string.confirm), dialogListener);
        dialog.setNegativeButton(getString(R.string.cancel), dialogListener);
        dialog.setSingleChoiceItems(type, checkedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

						/*
						 * if (getResources().getConfiguration().locale
						 * .getLanguage().equals("zh") &&
						 * getResources().getConfiguration().locale
						 * .getCountry().equals("CN")) {
						 */
                        for (int i = 0; i < type2.length; i++) {
                            if (which == i) {
                                nMainID = Integer.valueOf(type2[i][1]);
                                break;
                            }
                        }

                        // }
                    }
                });
        dialog.show();
    }

    /**
     * @Title: createAlertDialog @Description: TODO(这里用一句话描述这个方法的作用) @param @param
     * string @param @param object @param @return 设定文件 @return Builder
     * 返回类型 @throws
     */
    private Builder createAlertDialog(String title, String message) {
        // TODO Auto-generated method stub
        Builder dialog = new Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.create();
        return dialog;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isQuit == false) {
                isQuit = true;
                Toast.makeText(getBaseContext(), R.string.back_confirm, 2000)
                        .show();
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
            }
        }
        return true;
    }

    public DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (DIALOG_ID) {

                case 1:
                    if (dialog.BUTTON_POSITIVE == which) {
                        if (nMainID == 0) {
                            if (SharedPreferencesHelper.getInt(
                                    getApplicationContext(), "nMainId", 2) != 2) {
                                nMainID = SharedPreferencesHelper.getInt(
                                        getApplicationContext(), "nMainId", 2);
                            } else {
                                nMainID = 2;
                            }
                        }

                        SharedPreferencesHelper.putInt(getApplicationContext(),
                                "nMainId", nMainID);
                        dialog.dismiss();
                    } else if (dialog.BUTTON_NEGATIVE == which) {
                        dialog.dismiss();
                    }
                    break;

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 23) {
                CheckPermission checkPermission = new CheckPermission(this);
                if (checkPermission.permissionSet(PERMISSION)) {
                    PermissionActivity.startActivityForResult(this, 0,
                            SharedPreferencesHelper.getInt(
                                    getApplicationContext(), "nMainId", 2),
                            devcode, 0, 0,0, PERMISSION);
                } else {
                    Uri uri = data.getData();
                    selectPath = getPath(getApplicationContext(), uri);
                    RecogService.nMainID = SharedPreferencesHelper.getInt(
                            getApplicationContext(), "nMainId", 2);

                            new Thread() {
                    @Override
                    public void run() {
                        RecogService.isRecogByPath = true;
                        Intent recogIntent = new Intent(MainActivity.this,
                                RecogService.class);
                        bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
                    }
                }.start();

                    //ActivityRecogUtils.getRecogResult(MainActivity.this, selectPath, RecogService.nMainID, true);
                }
            } else {
                Uri uri = data.getData();
                selectPath = getPath(getApplicationContext(), uri);
                RecogService.nMainID = SharedPreferencesHelper.getInt(
                        getApplicationContext(), "nMainId", 2);
                new Thread() {
                    @Override
                    public void run() {
                        RecogService.isRecogByPath = true;
                        Intent recogIntent = new Intent(MainActivity.this,
                                RecogService.class);
                        bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
                    }
                }.start();
                //ActivityRecogUtils.getRecogResult(MainActivity.this, selectPath, RecogService.nMainID, true);

            }

        } else if (requestCode == 8 && resultCode == Activity.RESULT_OK) {
            //Activtiy recognize returned results
            int ReturnAuthority = data.getIntExtra("ReturnAuthority", -100000);//  get activation status
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.ReturnAuthority = data.getIntExtra("ReturnAuthority", -100000);//  get activation status
            resultMessage.ReturnInitIDCard = data
                    .getIntExtra("ReturnInitIDCard", -100000);// Get initialization return value
            resultMessage.ReturnLoadImageToMemory = data.getIntExtra(
                    "ReturnLoadImageToMemory", -100000);// Get the image return value
            resultMessage.ReturnRecogIDCard = data.getIntExtra("ReturnRecogIDCard",
                    -100000);// Get the recogniion reurn value
            resultMessage.GetFieldName = (String[]) data
                    .getSerializableExtra("GetFieldName");
            resultMessage.GetRecogResult = (String[]) data
                    .getSerializableExtra("GetRecogResult");
            ActivityRecogUtils.goShowResultActivity(MainActivity.this, resultMessage,0,selectPath,selectPath.substring(0,selectPath.indexOf(".jpg"))+ "Cut.jpg");
        }
    }

    //  Recognition Test
    public ServiceConnection recogConn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            recogBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            recogBinder = (RecogService.recogBinder) service;
            if(recogBinder!=null) {
                        RecogParameterMessage rpm = new RecogParameterMessage();
                        rpm.nTypeLoadImageToMemory = 0;
                        rpm.nMainID = SharedPreferencesHelper.getInt(
                                getApplicationContext(), "nMainId", 2);
                        rpm.nSubID = null;
                        rpm.GetSubID = true;
                        rpm.GetVersionInfo = true;
                        rpm.logo = "";
                        rpm.userdata = "";
                        rpm.sn = "";
                        rpm.authfile = "";
                        rpm.isCut = true;
                        rpm.triggertype = 0;
                        rpm.devcode = devcode;
                        //nProcessType：0- deleting all instructions 1- cropping 2- rotation  3- cropping and rotation
                        // 4- tilt correction 5- cropping+ tilt correction 6- rotation+tile correct
                        // 7- cropping+rotation+tilt correction.
                        rpm.nProcessType = 7;
                        rpm.nSetType = 1;// nSetType: 0－Deleting operations，1－setting operations
                        rpm.lpFileName = selectPath; // If rpm.lpFileName is null, auomatic recognition function will be executed.
                        // rpm.lpHeadFileName = selectPath;//Save portrait of identity document
                        rpm.isSaveCut = true;// Save cropping picture false=not saving  true=saving
                        rpm.cutSavePath="";
                        if (SharedPreferencesHelper.getInt(getApplicationContext(),
                                "nMainId", 2) == 2) {
                            rpm.isAutoClassify = true;
                            rpm.isOnlyClassIDCard = true;
                        } else if (SharedPreferencesHelper.getInt(getApplicationContext(),
                                "nMainId", 2) == 3000) {
                            rpm.nMainID = 1034;
                        }
                        // end
                        try {

                            ResultMessage resultMessage;
                            resultMessage = recogBinder.getRecogResult(rpm);
                            if (resultMessage.ReturnAuthority == 0
                                    && resultMessage.ReturnInitIDCard == 0
                                    && resultMessage.ReturnLoadImageToMemory == 0
                                    && resultMessage.ReturnRecogIDCard > 0) {
                                String iDResultString = "";
                                String[] GetFieldName = resultMessage.GetFieldName;
                                String[] GetRecogResult = resultMessage.GetRecogResult;

                                for (int i = 1; i < GetFieldName.length; i++) {
                                    if (GetRecogResult[i] != null) {
                                        if (!recogResultString.equals(""))
                                            recogResultString = recogResultString
                                                    + GetFieldName[i] + ":"
                                                    + GetRecogResult[i] + ",";
                                        else {
                                            recogResultString = GetFieldName[i] + ":"
                                                    + GetRecogResult[i] + ",";
                                        }
                                    }
                                }
                                Intent intent = new Intent(MainActivity.this,
                                        ShowResultActivity.class);
                                intent.putExtra("recogResult", recogResultString);
                                intent.putExtra("fullPagePath", selectPath);
                                intent.putExtra("importRecog", true);
                                intent.putExtra("cutPagePath", selectPath.substring(0, selectPath.indexOf(".jpg")) + "Cut.jpg");
                                MainActivity.this.finish();
                                startActivity(intent);
                            } else {
                                String string = "";
                                if (resultMessage.ReturnAuthority == -100000) {
                                    string = getString(R.string.exception)
                                            + resultMessage.ReturnAuthority;
                                } else if (resultMessage.ReturnAuthority != 0) {
                                    string = getString(R.string.exception1)
                                            + resultMessage.ReturnAuthority;
                                } else if (resultMessage.ReturnInitIDCard != 0) {
                                    string = getString(R.string.exception2)
                                            + resultMessage.ReturnInitIDCard;
                                } else if (resultMessage.ReturnLoadImageToMemory != 0) {
                                    if (resultMessage.ReturnLoadImageToMemory == 3) {
                                        string = getString(R.string.exception3)
                                                + resultMessage.ReturnLoadImageToMemory;
                                    } else if (resultMessage.ReturnLoadImageToMemory == 1) {
                                        string = getString(R.string.exception4)
                                                + resultMessage.ReturnLoadImageToMemory;
                                    } else {
                                        string = getString(R.string.exception5)
                                                + resultMessage.ReturnLoadImageToMemory;
                                    }
                                } else if (resultMessage.ReturnRecogIDCard <= 0) {
                                    if (resultMessage.ReturnRecogIDCard == -6) {
                                        string = getString(R.string.exception9);
                                    } else {
                                        string = getString(R.string.exception6)
                                                + resultMessage.ReturnRecogIDCard;
                                    }
                                }
                                Intent intent = new Intent(MainActivity.this,
                                        ShowResultActivity.class);
                                intent.putExtra("exception", string);
                                intent.putExtra("fullPagePath", selectPath);
                                intent.putExtra("importRecog", true);
                                MainActivity.this.finish();
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            Looper.prepare();
//                            Toast.makeText(getApplicationContext(),
//                                    getString(R.string.recognized_failed),
//                                    Toast.LENGTH_SHORT).show();
//                            Looper.loop();
                        } finally {
                            if (recogBinder != null) {
                                unbindService(recogConn);
                            }
                        }

            }

        }
    };

    private void startAuthService() {
        RecogService.isOnlyReadSDAuthmodeLSC = false;
        Intent authIntent = new Intent(MainActivity.this, AuthService.class);
        bindService(authIntent, authConn, Service.BIND_AUTO_CREATE);
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /***
     * @param @param context @param @param uri @param @return 设定文件 @return
     *               String 返回类型 @throws
     * @Title: getPath
     * @Description: TODO(这里用一句话描述这个方法的作用) 获取图片路径
     */

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) { // 忽略大小写
            // String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private String[][] intiList() {
        String[][] list = null;
        if (getResources().getConfiguration().locale.getLanguage().equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("CN")) {
            String[][] temp = {{"机读码", "3000"}, {"护照", "13"},
                    {"居民身份证", "2"}, {"临时身份证", "4"}, {"港澳通行证", "9"},
                    {"大陆居民往来台湾通行证", "11"}, {"签证", "12"},
                    {"新版港澳通行证", "22"}, {"中国驾照", "5"},{ "中国驾照副页", "28" }, {"中国行驶证", "6"},
                    {"香港身份证", "1001"},{ "户口本", "16" }, {"回乡证(正面)", "14"},
                    {"回乡证(背面)", "15"}, {"澳门身份证", "1005"},
                    {"新版澳门身份证", "1012"},  {"深圳市居住证", "1013"},{"台胞证", "10"},
                    {"新版台胞证(正面)", "25"}, {"新版台胞证(背面)", "26"},
                    {"台湾身份证(正面)", "1031"}, {"台湾身份证(背面)", "1032"},{"中国军官证", "7"},
                    {"全民健康保险卡", "1030"}, {"马来西亚身份证", "2001"},
                    {"新加坡身份证", "2004"}, {"新西兰驾照", "2003"},
                    {"加利福尼亚驾照", "2002"}, {"印度尼西亚身份证", "2010"}, {"泰国身份证", "2011"},{ "北京社保卡", "1021" }};
            list = temp;
        } else if (getResources().getConfiguration().locale.getLanguage()
                .equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("TW")) {
            String[][] temp = {{"機讀碼", "3000"}, {"護照", "13"},
                    {"居民身份證", "2"}, {"臨時身份證", "4"}, {"港澳通行證", "9"},
                    {"大陸居民往來台灣通行證", "11"}, {"簽證", "12"},
                    {"新版港澳通行證", "22"}, {"中國駕照", "5"},{ "中國駕照副頁", "28" }, {"中國行駛證", "6"},
                    {"香港身份證", "1001"}, { "戶口本", "16" },{"回鄉證(正面)", "14"},
                    {"回鄉證(背面)", "15"}, {"澳門身份證", "1005"},
                    {"新版澳門身份證", "1012"},  {"深圳市居住證", "1013"},{"台胞證", "10"},
                    {"台灣身份證(正面)", "1031"}, {"台灣身份證(背面)", "1032"},
                    {"新版台胞证(正面)", "25"}, {"新版台胞证(背面)", "26"},{"中國軍官証", "7"},
                    {"全民健康保險卡", "1030"}, {"馬來西亞身份證", "2001"},
                    {"新加坡身份證", "2004"}, {"新西蘭駕照", "2003"},
                    {"加利福尼亞駕照", "2002"}, {"印度尼西亞身份證", "2010"}, {"泰國身份證", "2011"},{ "北京社保卡", "1021" }};
            list = temp;
        } else {
            String[][] temp = {{"Machine readable zone", "3000"},
                    {"Passport", "13"}, {"Chinese ID card", "2"}, {"Interim ID card", "4"},
                    {"Exit-Entry Permit to HK/Macau", "9"},
                    {"Taiwan pass", "11"}, {"Visa", "12"},
                    {"e-EEP to HK/Macau", "22"},
                    {"Chinese Driving license", "5"},
                    { "Chinese Driving license(second)", "28" },
                    {"Chinese vehicle license", "6"},
                    {"HK ID card", "1001"},{ "Household Register", "16" },
                    {"Home return permit (Obverse)", "14"},
                    {"Home return permit (Reverse)", "15"},
                    {"Macau ID card", "1005"},
                    {"New Macau ID card", "1012"}, {"Shenzhen Resident Permit", "1013"},
                    {"To the Mainland Travel Permit", "10"},
                    {"Taiwan ID card (Obverse)", "1031"},
                    {"Taiwan ID card (Reverse)", "1032"},
                    {"To the New Mainland Travel Permit(Obverse)", "25"},
                    {"To the New Mainland Travel Permit(Reverse)", "26"},{"Chinese certificate of officers", "7"},
                    {"National health care card", "1030"},
                    {"MyKad", "2001"}, {"Singapore ID card", "2004"},
                    {"New Zealand Driving license", "2003"},
                    {"California driving license", "2002"}, {"Indonesia Identity Card", "2010"}, {"Thailand's Identity Card", "2011"} ,
                    {"Beijing social security card","1021"}};
            list = temp;
        }
        return list;
    }
}
