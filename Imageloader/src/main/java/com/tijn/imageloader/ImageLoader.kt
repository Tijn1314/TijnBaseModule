package com.tijn.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.tijn.imageloader.base.BaseImageLoaderStrategy
import com.tijn.imageloader.base.ImageBuilder
import com.tijn.imageloader.glide.GlideImageLoaderStrategy
import com.tijn.imageloader.listeners.ImageSaveListener
import com.tijn.imageloader.listeners.SourceReadyListener


object ImageLoader {
    private var strategy: BaseImageLoaderStrategy = GlideImageLoaderStrategy()

    fun loadImage(url: String?, imageView: ImageView?) {
        strategy.loadImage(url, imageView)
    }

    fun loadImage(url: String?, placeholder: Int?, imageView: ImageView?) {
        strategy.loadImage(url, placeholder, placeholder, imageView)
    }

    fun loadImage(url: String?, placeholder: Int?, error: Int?, imageView: ImageView?) {
        strategy.loadImage(url, placeholder, error, imageView)
    }

    fun loadImage(
        context: Context?,
        url: String?,
        placeholder: Int?,
        error: Int?,
        imageView: ImageView?
    ) {
        strategy.loadImage(context, url, placeholder, error, imageView)
    }

    fun loadImage(
        context: Context?,
        url: String?,
        width: Int?,
        height: Int?,
        sourceReadyListener: SourceReadyListener<Drawable>?
    ) {
        strategy.loadImage(context, url, width, height, sourceReadyListener)
    }

    fun loadImage(url: String?, imageView: ImageView?, imageBuilder: ImageBuilder?) {
        strategy.loadImage(url, imageView, imageBuilder)
    }

    /**
     * 加载圆形图片
     */
    fun loadCircleImage(context: Context?, url: String?, imageView: ImageView?) {
        strategy.loadCircleImage(context, url, imageView)
    }


    //清除硬盘缓存
    fun clearImageDiskCache(context: Context) {
        strategy.clearImageDiskCache(context)
    }

    //清除内存缓存
    fun clearImageMemoryCache(context: Context) {
        strategy.clearImageMemoryCache(context)
    }

    //根据不同的内存状态，来响应不同的内存释放策略
    fun trimMemory(context: Context, level: Int) {
        strategy.trimMemory(context, level)
    }

    //获取缓存大小
    fun getCacheSize(context: Context): String? {
        return strategy.getCacheSize(context)
    }

    fun saveImage(
        context: Context,
        url: String,
        savePath: String,
        saveFileName: String,
        listener: ImageSaveListener?
    ) {
        strategy.saveImage(context, url, savePath, saveFileName, listener)
    }


    fun setImageLoaderStrategy(imageLoaderStrategy: BaseImageLoaderStrategy) {
        if (imageLoaderStrategy == null) {
            return
        }
        strategy = imageLoaderStrategy
    }
}