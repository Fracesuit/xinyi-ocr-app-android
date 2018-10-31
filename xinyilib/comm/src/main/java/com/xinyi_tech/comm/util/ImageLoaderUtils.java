package com.xinyi_tech.comm.util;

import android.content.Intent;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xinyi_tech.comm.R;
import com.xinyi_tech.comm.base.BigImageActivity;


/**
 * Created by Fracesuit on 2017/7/28.
 */

public class ImageLoaderUtils {
    private static int placeHolderIcon = 0;
    private static int errorIcon = 0;
    private static int fallbackIcon = 0;


    public static void init(int defaultIcon) {
        placeHolderIcon = defaultIcon;
        errorIcon = defaultIcon;
        fallbackIcon = defaultIcon;
    }

    public static void init(int placeHolderIcon, int errorIcon, int fallbackIcon) {
        ImageLoaderUtils.placeHolderIcon = placeHolderIcon;
        ImageLoaderUtils.errorIcon = errorIcon;
        ImageLoaderUtils.fallbackIcon = fallbackIcon;
    }

    public static void showImage(ImageView imageView, Object imgUrl) {
        showImage(imageView, imgUrl, 0, null);
    }

    public static void showImage(ImageView imageView, Object imgUrl, int defaultIcon) {
        showImage(imageView, imgUrl, defaultIcon, null);
    }

    public static void showImage(ImageView imageView, Object imgUrl, int defaultIcon, RequestOptions requestOptions) {
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
        }
        requestOptions.placeholder(defaultIcon == 0 ? placeHolderIcon == 0 ? R.mipmap.comm_empty : placeHolderIcon : defaultIcon)
                .error(defaultIcon == 0 ? errorIcon == 0 ? R.mipmap.comm_empty : errorIcon : defaultIcon)
                .fallback(defaultIcon == 0 ? fallbackIcon == 0 ? R.mipmap.comm_empty : fallbackIcon : defaultIcon);
        Glide.with(imageView)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void showBigImage(Object imgUrl) {
        final Intent intent = new Intent(Utils.getApp(), BigImageActivity.class);
        if (imgUrl instanceof String) {
            intent.putExtra(BigImageActivity.DATA_PIC_PATH, (String) imgUrl);
        } else if (imgUrl instanceof Integer) {
            intent.putExtra(BigImageActivity.DATA_PIC_PATH, (Integer) imgUrl);
        }

        ActivityUtils2.startActivityByContext(intent);
    }

}
