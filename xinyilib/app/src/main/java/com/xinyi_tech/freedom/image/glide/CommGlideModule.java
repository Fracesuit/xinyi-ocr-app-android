package com.xinyi_tech.freedom.image.glide;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.xinyi_tech.comm.constant.CommConstant;
import com.xinyi_tech.freedom.app.FreedomApplication;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by zhiren.zhang on 2017/10/25.
 */

@GlideModule
public class CommGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, CommConstant.Path.PATH_PHOTO_CACHE, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
    }

    /* GlideApp.*/
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient okHttpClient = ((FreedomApplication) Utils.getApp()).getRetrofitManager().getOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
