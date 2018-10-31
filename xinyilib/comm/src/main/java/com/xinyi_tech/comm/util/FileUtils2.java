package com.xinyi_tech.comm.util;

import android.net.Uri;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xinyi_tech.comm.constant.CommConstant;

import java.io.File;


/**
 * Created by Fracesuit on 2017/7/31.
 */

public class FileUtils2 {

    static String currentUseName;

    public static void init(String useName) {
        currentUseName = useName;
    }

    public static Uri getPhotoUri() {
        String fileName = TimeUtils.getNowString(CommConstant.DeteFromat.DEFAULT_FORMAT_ALL_1) + ".jpg";
        return Uri.fromFile(new File(getRootFile(CommConstant.Path.PATH_PHOTO_CACHE), fileName));
    }

    public static File getPhotoDir() {
        return getRootFile(CommConstant.Path.PATH_PHOTO_CACHE);
    }

    public static File getDbFile(String dbName) {
        return new File(getRootFile(CommConstant.Path.PATH_DB_CACHE), dbName);
    }

    public static File getCompressCacheDir() {
        return getRootFile(CommConstant.Path.PATH_COMPRESS_CACHE);
    }


    public static File getNetCacheDir() {
        return getRootFile(CommConstant.Path.PATH_NET_CACHE);
    }


    public static File getRootFile(String functionPath) {
        String path = CommConstant.Path.PATH_ROOT;

        if (!StringUtils2.isEmpty(currentUseName)) {
            path += File.separator + currentUseName;
        }
        if (!StringUtils2.isEmpty(functionPath)) {
            path += File.separator + functionPath;
        }
        if (FileUtils.createOrExistsDir(path)) {
            return FileUtils.getFileByPath(path);
        }
        return null;
    }


    public static String getLastDirName(String dirPath) {
        int lastSep = dirPath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : dirPath.substring(lastSep + 1);
    }
}
