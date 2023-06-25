package com.mobile.diary

import android.app.Application
import com.tencent.mmkv.MMKV

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}