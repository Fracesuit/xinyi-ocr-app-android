package com.camera.internal.utils;

import android.os.Environment;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * Created by Fracesuit on 2017/7/14.
 */

public class SDCardUtils2 {

    //获取各种各样的路径
    //内部存储路径--不需要权限
    //data/data/0/com.it.suitapp/files
    public static File getInnerFile() {
        return Utils.getApp().getFilesDir();
    }

    ///data/data/0/com.it.suitapp/cache
    public static File getInnerCache() {
        return Utils.getApp().getCacheDir();
    }


    private static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //外部存储路径--私有--不需要权限
    //storage/emulated/0/Android/data/com.it.suitapp/files    /Movies
    public static File getExternalPrivateFile(String type) {
        return isSDCardEnable() ? Utils.getApp().getExternalFilesDir(type) : getInnerFile();
    }

    ///storage/emulated/0/Android/data/com.it.suitapp/cache
    public static File getExternalPrivateCache() {
        return isSDCardEnable() ? Utils.getApp().getExternalCacheDir() : getInnerCache();
    }


    //外部存储路径--公有--需要权限  和SDCardUtils  中有重复
    // /storage/emulated/0
    ///storage/emulated/0/Movies   Environment.DIRECTORY_MOVIES
    public static File getExternalPublic(String type) {
        if (type != null) {
            File file = isSDCardEnable() ? Environment.getExternalStoragePublicDirectory(type) : getExternalPrivateCache();
            return file;
        } else {
            return isSDCardEnable() ? Environment.getExternalStorageDirectory() : getExternalPrivateCache();
        }
    }
}
