package com.badzohugues.staticlbcapp

import android.app.Application
import com.badzohugues.staticlbcapp.misc.NetworkHelper

class StaticLBCApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkHelper.init(this)
    }
}