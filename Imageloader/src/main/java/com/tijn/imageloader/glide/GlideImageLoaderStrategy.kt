package com.tijn.imageloader.glide

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.tijn.imageloader.R
import com.tijn.imageloader.base.BaseImageLoaderStrategy
import com.tijn.imageloader.base.ImageBuilder
import com.tijn.imageloader.listeners.ImageSaveListener
import com.tijn.imageloader.listeners.ProgressListener
import com.tijn.imageloader.listeners.SourceReadyListener
import com.tijn.imageloader.okhttp.ProgressInterceptor
import com.tijn.imageloader.util.CommonUtils.getFolderSize
import com.tijn.imageloader.util.CommonUtils.getFormatSize
import com.tijn.imageloader.util.CommonUtils.getPicType
import com.tijn.imageloader.util.CommonUtils.isSDCardExist
import java.io.*

class GlideImageLoaderStrategy : BaseImageLoaderStrategy {


    override fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(imageView?.context, url, defaultPlaceholder(), defaultPlaceholder(), imageView)
    }

    override fun loadImage(url: String?, placeholder: Int?, error: Int?, imageView: ImageView?) {
        loadImage(imageView?.context, url, placeholder, error, imageView)
    }

    override fun loadImage(
        context: Context?,
        url: String?,
        placeholder: Int?,
        error: Int?,
        imageView: ImageView?
    ) {
        if (url.isNullOrEmpty() || imageView == null) {
            return
        }
        Glide.with(context ?: imageView.context)
            .load(url)
            .placeholder(placeholder ?: defaultPlaceholder())
            .error(error ?: defaultError())
            .into(imageView)
    }

    override fun loadImage(
        context: Context?,
        url: String?,
        width: Int?,
        height: Int?,
        sourceReadyListener: SourceReadyListener<Drawable>?
    ) {
        if (url.isNullOrEmpty() || context == null) {
            return
        }
        Glide.with(context)
            .load(url)
            .into(object : CustomTarget<Drawable>(
                if (width != null && width > 0) width else SIZE_ORIGINAL,
                if (height != null && height > 0) height else SIZE_ORIGINAL
            ) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    sourceReadyListener?.onResourceReady(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    sourceReadyListener?.onLoadFailed()
                }
            })
    }

    override fun loadImage(url: String?, imageView: ImageView?, imageBuilder: ImageBuilder?) {
        if (url.isNullOrEmpty() || imageView == null) {
            return
        }
        val glide = Glide.with(imageView.context)
            .load(url)
        if (imageBuilder == null) {
            glide.placeholder(defaultPlaceholder())
            glide.error(defaultError())
        } else {
            if (imageBuilder.placeholder != 0) {
                glide.placeholder(if (imageBuilder.placeholder > 0) imageBuilder.placeholder else defaultPlaceholder())
            }
            if (imageBuilder.error != 0) {
                glide.error(if (imageBuilder.error > 0) imageBuilder.error else defaultError())
            }
            if (imageBuilder.radius > 0) {
                val transformation = GlideBorderRoundTransformation(imageView.context)
                    .setRadius(imageBuilder.radius)
                    .setMargin(imageBuilder.margin)
                    .setBorderWidth(imageBuilder.borderWidth)
                    .setBorderColor(imageBuilder.borderColor)
                    .setCornerPos(imageBuilder.cornerPos)
                    .setScaleType(imageBuilder.scaleType ?: imageView.scaleType)
                glide.transform(transformation)
            }
            imageBuilder.bitmapConfig?.let {
                when (it) {
                    Bitmap.Config.RGB_565 -> glide.format(DecodeFormat.PREFER_RGB_565)
                    else -> glide.format(DecodeFormat.PREFER_ARGB_8888)
                }

            }
            if (imageBuilder.sourceReadyListener != null) {
                glide.listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageBuilder.sourceReadyListener.onLoadFailed()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            imageBuilder.sourceReadyListener.onResourceReady(it)
                        }
                        return false
                    }
                })
            }
        }
        glide.into(imageView)
    }

    override fun loadCircleImage(context: Context?, url: String?, imageView: ImageView?) {
        if (url.isNullOrEmpty() || imageView == null) {
            return
        }
        Glide.with(context ?: imageView.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(defaultPlaceholder())
            .error(defaultError())
            .into(imageView)
    }


    override fun clearImageDiskCache(context: Context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread { Glide.get(context.applicationContext).clearDiskCache() }.start()
            } else {
                Glide.get(context.applicationContext).clearDiskCache()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun clearImageMemoryCache(context: Context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.applicationContext).clearMemory()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun trimMemory(context: Context, level: Int) {
        Glide.get(context).trimMemory(level)
    }

    override fun getCacheSize(context: Context): String {
        try {
            return getFormatSize(getFolderSize(Glide.getPhotoCacheDir(context.applicationContext)!!).toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun saveImage(
        context: Context,
        url: String,
        savePath: String,
        saveFileName: String,
        listener: ImageSaveListener?
    ) {
        if (!isSDCardExist() || TextUtils.isEmpty(url)) {
            listener!!.onSaveFail()
            return
        }
        var fromStream: InputStream? = null
        var toStream: OutputStream? = null
        try {
            val cacheFile = Glide.with(context).load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            if (cacheFile == null || !cacheFile.exists()) {
                listener?.onSaveFail()
                return
            }
            val dir = File(savePath)
            if (!dir.exists()) {
                dir.mkdir()
            }
            val file = File(dir, saveFileName + getPicType(cacheFile.absolutePath))
            fromStream = FileInputStream(cacheFile)
            toStream = FileOutputStream(file)
            val length = ByteArray(1024)
            var count: Int
            while (fromStream.read(length).also { count = it } > 0) {
                toStream.write(length, 0, count)
            }
            //用广播通知相册进行更新相册
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val uri = Uri.fromFile(file)
            intent.data = uri
            context.sendBroadcast(intent)
            listener?.onSaveSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            listener?.onSaveFail()
        } finally {
            try {
                fromStream?.close()
                toStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun defaultPlaceholder(): Int {
        return R.drawable.common_placeholder_loading
    }

    override fun defaultError(): Int {
        return R.drawable.common_placeholder_loading
    }
}