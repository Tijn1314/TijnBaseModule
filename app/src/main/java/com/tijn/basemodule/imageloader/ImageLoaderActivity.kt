package com.tijn.basemodule.imageloader

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tijn.basemodule.R
import com.tijn.imageloader.ImageLoader
import com.tijn.imageloader.listeners.ProgressListener
import com.tijn.imageloader.okhttp.ProgressInterceptor


class ImageLoaderActivity : AppCompatActivity() {
    val url =
        "https://pics7.baidu.com/feed/f3d3572c11dfa9ecf2e4085303ae120a908fc130.jpeg?token=a4b811890769a87bf1b6b558437e669f"
    val gifUrl =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg95.699pic.com%2Fphoto%2F40142%2F4204.gif_wh860.gif&refer=http%3A%2F%2Fimg95.699pic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1647673283&t=2f1cf5f343d22262acc6bd0245d3ad1b"
    var iv: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageload)
        iv = findViewById(R.id.img)

        ProgressInterceptor.addListener(gifUrl, object : ProgressListener {
            override fun onProgress(progress: Int) {
                Log.e("MainImg", progress.toString())
            }

        })
        iv?.let {
            ImageLoader.loadCircleImage(this, url, it)
//            ImageLoader.loadImage(
//                gifUrl,
//                it,
//                ImageBuilder()
//                    .setRadius(30)
//                    .setCornerPos(Integer.valueOf("1001",2))
////                    .setScaleType(it.scaleType)
//                    .setSourceReadyListener(object : SourceReadyListener<Drawable> {
//                    override fun onResourceReady(resource: Drawable) {
//                        Log.e("MainImg", "onResourceReady")
//                    }
//
//                    override fun onLoadFailed() {
//                        Log.e("MainImg", "onLoadFailed")
//                    }
//                })
//            )
        }

//        ImageLoader.loadImage(this, gifUrl, 100, 100, object : SourceReadyListener<Drawable> {
//            override fun onResourceReady(resource: Drawable) {
//                iv?.setImageDrawable(resource)
//                ProgressInterceptor.removeListener(gifUrl)
//            }
//
//            override fun onLoadFailed() {
//            }
//
//        })
    }

}