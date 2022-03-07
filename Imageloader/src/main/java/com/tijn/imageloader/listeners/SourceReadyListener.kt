package com.tijn.imageloader.listeners

import android.graphics.drawable.Drawable

/**
 * 通知准备就绪
 */
interface SourceReadyListener<T> {
    fun onResourceReady(resource: T)
    fun onLoadFailed()
}