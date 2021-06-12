package com.badzohugues.staticlbcapp

import android.app.Application
import com.badzohugues.staticlbcapp.misc.NetworkHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StaticLBCApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkHelper.init(this)
    }
}
