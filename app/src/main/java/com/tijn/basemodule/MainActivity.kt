package com.tijn.basemodule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tijn.basemodule.R.layout.activity_main
import com.tijn.basemodule.imageloader.ImageLoaderActivity
import com.tijn.basemodule.net.download.DownloadActivity
import com.tijn.basemodule.net.home.HomeActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val imageLoaderActivity: TextView by lazy { findViewById(R.id.imageLoaderActivity) }
    private val homeActivity: TextView by lazy { findViewById(R.id.homeActivity) }
    private val downloadActivity: TextView by lazy { findViewById(R.id.downloadActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        imageLoaderActivity.setOnClickListener(this)
        homeActivity.setOnClickListener(this)
        downloadActivity.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageLoaderActivity -> {
                startActivity(Intent(this, ImageLoaderActivity::class.java))
            }
            R.id.homeActivity -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.downloadActivity -> {
                startActivity(Intent(this, DownloadActivity::class.java))
            }
        }
    }


}