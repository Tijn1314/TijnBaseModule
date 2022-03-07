package com.tijn.imageloader.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.tijn.imageloader.okhttp.ProgressInterceptor;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public class CommonAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //添加拦截器到Glide
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();

        //原来的是  new OkHttpUrlLoader.Factory()；
        registry.replace(
                GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(okHttpClient)
        );
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        try {
            //内存缓存
            long cacheSize = Runtime.getRuntime().maxMemory() / 10;
            builder.setMemoryCache(new LruResourceCache(cacheSize));
            //磁盘缓存
            builder.setDiskCache(
                    new DiskLruCacheFactory(
                            context.getCacheDir().getAbsolutePath() + File.separator + "glide", 1024 * 1024 * 200
                    )
            );
        } catch (Exception e) {
        }
    }
}
