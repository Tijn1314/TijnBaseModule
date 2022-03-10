package com.awedxr.base

import android.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tijn.baseui.utils.StatusBarHelper


open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val checkStatusBar = checkStatusBar()
        super.onCreate(savedInstanceState)
        if (checkStatusBar) {
            checkFitSystemWindow()
        }
    }


    protected open fun checkStatusBar(): Boolean {
        // 4.4以下不适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (translucentStatusBar()) {
                // 透明状态栏，内容伸展到状态栏内部
                StatusBarHelper.setStatusBarTranslucent(this)
                return true
            }
        }
        return false
    }

    protected open fun checkFitSystemWindow() {
        if (!translucentFull()) {
            val contentView = findViewById<View>(R.id.content) as ViewGroup
            val firstChild = contentView.getChildAt(0)
            if (firstChild != null) {
                firstChild.fitsSystemWindows = true
            }
            // 非全屏状态，尝试使用自定义的状态栏颜色
            var stasusBarColor: Int = Color.parseColor("#40000000")
            if (stasusBarColor != null) {
                if (statusBarLightMode()) {
                    if (!StatusBarHelper.setStatusBarLightMode(this)) {
                        stasusBarColor = Color.TRANSPARENT
                    }
                } else {
                    if (!StatusBarHelper.setStatusBarDarkMode(this)) {
                        stasusBarColor = Color.TRANSPARENT
                    }
                }
                StatusBarHelper.setStatusBarColor(this, stasusBarColor)
            }
        }
    }


    open fun statusBarLightMode(): Boolean {
        return true
    }

    open fun translucentFull(): Boolean {
        val windowParams = window.attributes
        return windowParams.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN > 0
    }

    open fun translucentStatusBar(): Boolean {
        return true
    }
}