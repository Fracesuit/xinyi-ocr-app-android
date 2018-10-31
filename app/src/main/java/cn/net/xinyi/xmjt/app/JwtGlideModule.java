package cn.net.xinyi.xmjt.app;

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

import java.io.InputStream;


/**
 * 设置glide加载引擎和缓存路径
 */

@GlideModule
public class JwtGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //final UserModel appUserModel = UserManager.getLoginInfo();
        String path = "";
       /* if (appUserModel != null) {
            path = appUserModel.getUsername() + "/";
        }*/
        path += CommConstant.Path.PATH_COMPRESS_CACHE;
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, path, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        JwtApplication xinyiApplication = (JwtApplication) Utils.getApp();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(xinyiApplication.getRetrofitManager().getOkHttpClient()));
    }
}
