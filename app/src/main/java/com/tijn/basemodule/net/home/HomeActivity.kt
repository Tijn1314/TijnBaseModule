package com.tijn.basemodule.net.home

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tijn.basemodule.R.layout.activity_main
import com.tijn.basemodule.net.home.Banner
import com.tijn.basemodule.net.home.HomeViewModel
import com.tijn.imageloader.ImageLoader
import com.tijn.imageloader.listeners.ProgressListener
import com.tijn.imageloader.okhttp.ProgressInterceptor


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        initData()
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun initData() {
        viewModel.apply { getBanner() }

        viewModel.getBannerLiveData()
            .observe(this,
                Observer<List<Banner>> {
                    Log.e("aaa", it.toString())
                })

    }
}