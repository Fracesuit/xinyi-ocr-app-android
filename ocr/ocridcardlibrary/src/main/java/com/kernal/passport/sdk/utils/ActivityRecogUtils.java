package com.kernal.passport.sdk.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.kernal.passportreader.sdk.MainActivity;
import com.kernal.passportreader.sdk.R;
import com.kernal.passportreader.sdk.ShowResultActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;

/**
 * Created by huangzhen on 2017/2/6.
 */

public class ActivityRecogUtils {
    /**
     *Invoking Activity recognition with animation effect
     * @param context Context
     * @param selectPath The path to recognize images.
     * @param nMainID Document type to be recognized.
     * @param cutBoolean To see if cropping is needed.
     */
    public static void getRecogResult(Context context, String selectPath, int nMainID, boolean cutBoolean) {
        try {
            RecogService.isRecogByPath = true;
            String logopath = "";
            // String logopath = getSDPath() + "/photo_logo.png";
            Intent intent = new Intent("kernal.idcard");
            Bundle bundle = new Bundle();
            int nSubID[] = null;// {0x0001};
            bundle.putBoolean("isGetRecogFieldPos", false);// To see if we get the location of recognized fields. If the default one is untrue, it means not getting.
            // It must be cropped in the images which have been clipped by core recognition engine.
            bundle.putString("cls", "checkauto.com.IdcardRunner");
            bundle.putInt("nTypeInitIDCard", 0); // To save, press “0”.
            bundle.putString("lpFileName", selectPath);//  Designated image path.
            bundle.putString("cutSavePath", "");// Storage path of cropping images.
            bundle.putInt("nTypeLoadImageToMemory", 0);// ”0” stands for unceratian images, “1” for visual ight, “2” for IR, “4” for UV.
            // if (nMainID == 1000) {
            // nSubID[0] = 3;
            // }
            bundle.putInt("nMainID", nMainID); // Mian type of documents. “6” stands for driving license, “2” for 2nd generation Identity card of P.R. Here one main document type can be uploaded. Each type of documents has its only unique ID No.. The possible value can be seen on the notes of main document type.
            bundle.putIntArray("nSubID", nSubID); // Save the subtype ID of recognized documents. The subtype ones of each document can be shown in “ Subtype document instruction”. IfnSubID[0]=null, it means to set main type douments as “nMainID”.
            // bundle.putBoolean("GetSubID", true);
            // //Get the subtype ID of recognized images.
            // bundle.putString("lpHeadFileName",
            // "/mnt/sdcard/head.jpg");//In saving the path, the suffix can only be jpg, bmp, tif.
            bundle.putBoolean("GetVersionInfo", true); //Get teh version information of development kit.
            //bundle.putBoolean("isSetIDCardRejectType", true);
            bundle.putString("sn", "");
            // bundle.putString("datefile",
            // "assets");//Environment.getExternalStorageDirectory().toString()+"/wtdate.lsc"
            bundle.putString("devcode", Devcode.devcode);
            // bundle.putBoolean("isCheckDevType", true); // To test device type switch in a compulsive way.
            // bundle.putString("versionfile",
            // "assets");//Environment.getExternalStorageDirectory().toString()+"/wtversion.lsc"
            // bundle.putString("sn", "XS4XAYRWEFRY248YY4LHYY178");
            // //Serial No. activation method,  XS4XAYRWEFRY248YY4LHYY178 has been used.
            // bundle.putString("server",
            // "http://192.168.0.36:8080");//http://192.168.0.36:8888
            // bundle.putString("authfile", ""); // File activation method.  //
            // /mnt/sdcard/AndroidWT/357816040594713_zj.txt
            if(nMainID==2) {
                bundle.putBoolean("isAutoClassify", true);
                bundle.putBoolean("isOnlyClassIDCard", true);
            }
            bundle.putString("logo", logopath); // The path of logo. Logo is shown on the top right corner of awaiting pages.
            bundle.putInt("nProcessType", 7);//2- rotation  3- cropping and rotation 4- tilt correction 5- cropping+ tilt correction 6- rotation+tile correct 7- cropping+rotation+tilt correction.
            bundle.putInt("nSetType", 1);// nSetType:“0”-cancelling the operations, 1- setting operations.
            bundle.putBoolean("isCut", cutBoolean); // If not setting, this item will be defualt automatic cropping.
            bundle.putBoolean("isSaveCut", true);// To see if the cropping images should be saved.
            bundle.putString("returntype", "withvalue");// Withvalue is a returned value mode with paramaters( new value passed mode.
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, 8);
        } catch (Exception e) {
            Toast.makeText(
                    context,
                    context.getString(R.string.noFoundProgram)
                            + "wintone.idcard", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * .Get corresponding recognition results and skip to the result displaying interface.
     * @param context Context
     * @param resultMessage Recognize the returned paramater.
     * @param VehicleLicenseflag The primary interface is the one of driving license or general primary interface.
     */
    public static void goShowResultActivity(Context context,ResultMessage resultMessage,int VehicleLicenseflag,String fullPicturePath,String cutPicturePath) {
        try {
            String recogResultString = "";
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
                Intent intent = new Intent(context,
                        ShowResultActivity.class);
                intent.putExtra("recogResult", recogResultString);
                intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
                intent.putExtra("fullPagePath", fullPicturePath);
                intent.putExtra("cutPagePath", cutPicturePath);
                intent.putExtra("importRecog", true);
                ((Activity)context).finish();
                ((Activity)context).startActivity(intent);
            } else {
                String string = "";
                if (resultMessage.ReturnAuthority == -100000) {
                    string = context.getString(R.string.exception)
                            + resultMessage.ReturnAuthority;
                } else if (resultMessage.ReturnAuthority != 0) {
                    string = context.getString(R.string.exception1)
                            + resultMessage.ReturnAuthority;
                } else if (resultMessage.ReturnInitIDCard != 0) {
                    string = context.getString(R.string.exception2)
                            + resultMessage.ReturnInitIDCard;
                } else if (resultMessage.ReturnLoadImageToMemory != 0) {
                    if (resultMessage.ReturnLoadImageToMemory == 3) {
                        string = context.getString(R.string.exception3)
                                + resultMessage.ReturnLoadImageToMemory;
                    } else if (resultMessage.ReturnLoadImageToMemory == 1) {
                        string = context.getString(R.string.exception4)
                                + resultMessage.ReturnLoadImageToMemory;
                    } else {
                        string = context.getString(R.string.exception5)
                                + resultMessage.ReturnLoadImageToMemory;
                    }
                } else if (resultMessage.ReturnRecogIDCard <= 0) {
                    if (resultMessage.ReturnRecogIDCard == -6) {
                        string = context.getString(R.string.exception9);
                    } else {
                        string = context.getString(R.string.exception6)
                                + resultMessage.ReturnRecogIDCard;
                    }
                }
                Intent intent = new Intent(context,
                        ShowResultActivity.class);
                intent.putExtra("exception", string);
                intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
                ((Activity)context).finish();
                ((Activity)context).startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    context.getString(R.string.recognized_failed),
                    Toast.LENGTH_SHORT).show();

        }

    }
}
