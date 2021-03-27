package com.techlogix.pacabs_driver

import android.app.Activity
import android.app.Application
import android.content.Context

class PacabDriver : Application() {

    private var baseActivity: Activity? = null

    companion object {
        var INSTANCE: PacabDriver? = null
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        context = this

    }

    fun setActivity(baseActivity: Activity) {
        this.baseActivity = baseActivity
    }

    fun getBaseActivity(): Activity? {
        return baseActivity
    }
}