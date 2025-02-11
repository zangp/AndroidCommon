package com.perry.baselib.app

import android.app.Application
import com.perry.baselib.utils.AppStackManager

/**
 * @Author peng.zang
 * @Date 2025/2/7 16:25
 * @Description
 * @Version 1.0
 **/
open class BaseApplication : Application() {

    companion object {
        @Volatile
        private lateinit var ins: BaseApplication
        fun get() = ins
    }

    override fun onCreate() {
        super.onCreate()
        ins = this
        if (isOpenAppStack()) {
            AppStackManager.registerActivityLifecycle()
        }
    }

    open fun isOpenAppStack(): Boolean = false
}