package com.tijn.netlibrary

import android.app.Application

object NetConfig {
    var application: Application? = null

    fun initNet(app: Application) {
        application = app
    }
}