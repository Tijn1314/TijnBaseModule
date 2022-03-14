package com.tijn.basemodule.application

import android.app.Application
import com.tijn.netlibrary.NetConfig


/**
 * Create by liwen on 2020-05-18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetConfig.initNet(this)

    }
}