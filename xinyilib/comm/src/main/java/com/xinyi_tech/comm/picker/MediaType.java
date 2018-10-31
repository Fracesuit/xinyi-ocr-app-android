package com.xinyi_tech.comm.picker;

import android.text.TextUtils;

import com.luck.picture.lib.tools.DebugUtil;

import java.io.File;

/**
 * Created by zhiren.zhang on 2018/3/27.
 */

public class MediaType {

    public static final int TYPE_ADD = 0;
    public static final int TYPE_REMOVE = -1;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_ATTACHMENT = 4;


    public static int ofAdd() {
        return TYPE_ADD;
    }

    public static int ofRemove() {
        return TYPE_REMOVE;
    }


    public static int ofAttachment() {
        return TYPE_ATTACHMENT;
    }

    public static int ofImage() {
        return TYPE_IMAGE;
    }

    public static int ofVideo() {
        return TYPE_VIDEO;
    }

    public static int ofAudio() {
        return TYPE_AUDIO;
    }


    public static int getMediaTypeByPath(String path) {
        String pictureType = pathToType(path);

        switch (pictureType) {
            case "image/png":
            case "image/PNG":
            case "image/jpeg":
            case "image/JPEG":
            case "image/webp":
            case "image/WEBP":
            case "image/gif":
            case "image/bmp":
            case "image/GIF":
            case "imagex-ms-bmp":
                return TYPE_IMAGE;
            case "video/3gp":
            case "video/3gpp":
            case "video/3gpp2":
            case "video/avi":
            case "video/mp4":
            case "video/quicktime":
            case "video/x-msvideo":
            case "video/x-matroska":
            case "video/mpeg":
            case "video/webm":
            case "video/mp2ts":
                return TYPE_VIDEO;
            case "audio/mpeg":
            case "audio/x-ms-wma":
            case "audio/x-wav":
            case "audio/amr":
            case "audio/wav":
            case "audio/aac":
            case "audio/mp4":
            case "audio/quicktime":
            case "audio/lamr":
            case "audio/3gpp":
                return TYPE_AUDIO;
        }
        return TYPE_IMAGE;
    }


    /**
     * 是否是网络图片
     *
     * @param path
     * @return
     */
    public static boolean isHttp(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (path.startsWith("http")
                    || path.startsWith("https")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断文件类型是图片还是视频
     *
     * @return
     */
    private static String pathToType(String path) {
        File file = new File(path);
        if (file != null) {
            String name = file.getName();
            DebugUtil.i("**** fileToType:", name);
            if (name.endsWith(".mp4") || name.endsWith(".avi")
                    || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")) {
                return "video/mp4";
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                    || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
                    || name.endsWith(".bmp")) {
                return "image/jpeg";
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac") || name.endsWith(".lamr")) {
                return "audio/mpeg";
            }
        }
        return "image/jpeg";
    }
}
