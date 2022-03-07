package com.tijn.imageloader.listeners

/**
 * 图片保存监听器
 */
interface ImageSaveListener {
    fun onSaveSuccess()

    fun onSaveFail()
}