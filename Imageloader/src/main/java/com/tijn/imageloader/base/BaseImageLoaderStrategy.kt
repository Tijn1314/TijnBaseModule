package com.tijn.imageloader.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.tijn.imageloader.listeners.ImageSaveListener
import com.tijn.imageloader.listeners.SourceReadyListener

interface BaseImageLoaderStrategy {
    fun defaultPlaceholder(): Int
    fun defaultError(): Int

    //无占位图
    fun loadImage(url: String?, imageView: ImageView?)

    fun loadCircleImage(context: Context?, url: String?, imageView: ImageView?)

    fun loadImage(url: String?, placeholder: Int?, error: Int?, imageView: ImageView?)

    fun loadImage(
        context: Context?,
        url: String?,
        placeholder: Int?,
        error: Int?,
        imageView: ImageView?
    )

    fun loadImage(
        context: Context?,
        url: String?,
        width: Int?,
        height: Int?,
        sourceReadyListener: SourceReadyListener<Drawable>?
    )

    fun loadImage(url: String?, imageView: ImageView?, imageBuilder: ImageBuilder?)


    //清除硬盘缓存
    fun clearImageDiskCache(context: Context)

    //清除内存缓存
    fun clearImageMemoryCache(context: Context)

    //根据不同的内存状态，来响应不同的内存释放策略
    fun trimMemory(context: Context, level: Int)

    //获取缓存大小
    fun getCacheSize(context: Context): String?

    fun saveImage(
        context: Context,
        url: String,
        savePath: String,
        saveFileName: String,
        listener: ImageSaveListener?
    )


}