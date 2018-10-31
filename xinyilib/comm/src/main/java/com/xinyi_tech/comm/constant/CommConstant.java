package com.xinyi_tech.comm.constant;


import com.camera.internal.utils.SDCardUtils2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by Fracesuit on 2017/7/14.
 */

public class CommConstant {
    public static class Path {
        //图片 glide   压缩  数据库  网络
        // PATH_ROOT  +  USENAME+  功能名称+具体文件
        public static final String PATH_ROOT = SDCardUtils2.getExternalPrivateCache().getPath();
        public static final String PATH_PHOTO_CACHE = "photo";//存放图片的路径
        public static final String PATH_COMPRESS_CACHE = "compress";//图片的压缩路径
        public static final String PATH_DB_CACHE = "db";//数据库路径
        public static final String PATH_NET_CACHE = "net";//网络缓存路径
    }

    public static class DeteFromat {
        public static DateFormat DEFAULT_FORMAT_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_ALL_1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_ALL_2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_DAY_1 = new SimpleDateFormat("MM月dd日yyyy", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_DAY_2 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_MINUTE = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_MINUTE_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        public static DateFormat DEFAULT_FORMAT_SECOND = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    }


}
