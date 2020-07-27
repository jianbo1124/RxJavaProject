package com.chen.rxjavaproject

import android.app.Application
import android.content.Context

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
class MyApplication : Application() {
    companion object {
        var instance: MyApplication? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }
}