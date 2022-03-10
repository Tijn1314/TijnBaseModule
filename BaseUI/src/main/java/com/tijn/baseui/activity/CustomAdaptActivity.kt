package com.awedxr.base

import me.jessyan.autosize.internal.CustomAdapt

/**
 * Customize the adaptation parameters of the Activity
 */
open class CustomAdaptActivity : BaseActivity(), CustomAdapt {
    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return 1080f
    }
}