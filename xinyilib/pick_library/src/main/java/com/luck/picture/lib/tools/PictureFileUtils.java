package com.luck.picture.lib.tools;

import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.*;
import com.camera.internal.utils.SDCardUtils2;
import com.luck.picture.lib.config.PictureConfig;

import java.io.File;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.tools
 * email：893855882@qq.com
 * data：2017/5/30
 */

public class PictureFileUtils {
    public static final String POSTFIX = ".jpg";
    public static final String POST_VIDEO = ".mp4";
    public static final String POST_AUDIO = ".mp3";
    public static final String APP_NAME = "PickAnything";
    public static final String CAMERA_PATH = "PickImage";
    public static final String CAMERA_AUDIO_PATH = "PickAudio";
    public static final String CAMERA_VIDEO_PATH = "PickVideo";
    public static final String CROP_PATH = "PickCropImage";
    public static final String COMPRESS_PATH = "PickCompressImage";
    public static final String DOWNLOAD_PATH = "PickDownLoad";

    private static String rootPathDir = new File(SDCardUtils2.getExternalPublic(Environment.DIRECTORY_PICTURES), APP_NAME).getAbsolutePath();

    public static void init(String rootPathDir) {
        if (!com.blankj.utilcode.util.StringUtils.isEmpty(rootPathDir)) {
            PictureFileUtils.rootPathDir = rootPathDir;
        }

    }


    public static File createCameraFile(int type, String format) {
        String path;
        if (type == PictureConfig.TYPE_AUDIO) {
            path = getAudioDir();
            format = POST_AUDIO;
        } else if (type == PictureConfig.TYPE_VIDEO) {
            path = getVideoDir();
            format = POST_VIDEO;
        } else {
            path = getImageDir();
            format = TextUtils.isEmpty(format) ? POSTFIX : format;
        }
        String fileName = getFileName(format);
        return createFile(path, fileName);

    }


    private PictureFileUtils() {
    }


    public static String getDCIMCameraPath() {
        String absolutePath;
        try {
            absolutePath = "%" + Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return absolutePath;
    }

    /**
     * set empty PictureSelector Cache
     */
    public static void deleteCacheDirFile() {
        FileUtils.deleteDir(getRootDir(null));
    }


    public static File getRootDir(String chlidDir) {
        File pFile;
        if (TextUtils.isEmpty(chlidDir)) {
            pFile = new File(rootPathDir);
        } else {
            pFile = new File(rootPathDir, chlidDir);
        }
        return FileUtils.createOrExistsDir(pFile) ? pFile : null;
    }

    public static String getCompressDir() {
        File file = new File(getRootDir(null), COMPRESS_PATH);
        return FileUtils.createOrExistsDir(file) ? file.getAbsolutePath() : null;
    }

    public static String getCropDir() {
        File file = new File(getRootDir(null), CROP_PATH);
        return FileUtils.createOrExistsDir(file) ? file.getAbsolutePath() : null;
    }

    private static String getImageDir() {
        File file = new File(getRootDir(null), CAMERA_PATH);
        return FileUtils.createOrExistsDir(file) ? file.getAbsolutePath() : null;
    }

    private static String getVideoDir() {
        File file = new File(getRootDir(null), CAMERA_VIDEO_PATH);
        return FileUtils.createOrExistsDir(file) ? file.getAbsolutePath() : null;
    }

    private static String getAudioDir() {
        File file = new File(getRootDir(null), CAMERA_AUDIO_PATH);
        return FileUtils.createOrExistsDir(file) ? file.getAbsolutePath() : null;
    }


    //12051551_1254.jpg
    public static String getFileName(String fileFormat) {
        return TimeUtils.getNowMills() + "_" + (int) (Math.random() * 1000) + fileFormat;
    }

   /* public static File createFile(File file) {
        return FileUtils.createOrExistsFile(file) ? file : null;
    }*/

    public static File createFile(String dir, String fileNmae) {
        return new File(dir, fileNmae);
    }
}
