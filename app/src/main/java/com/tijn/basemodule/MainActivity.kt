package com.tijn.basemodule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tijn.basemodule.bean.BindingEntry
import com.tijn.basemodule.databinding.ActivityMainBinding
import com.tijn.basemodule.imageloader.ImageLoaderActivity
import com.tijn.basemodule.net.download.DownloadActivity
import com.tijn.basemodule.net.home.HomeActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.bindingEntry = BindingEntry("Aaaaaa")
        binding?.imageLoaderActivity?.setOnClickListener(this)
        binding?.homeActivity?.setOnClickListener(this)
        binding?.downloadActivity?.setOnClickListener(this)

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