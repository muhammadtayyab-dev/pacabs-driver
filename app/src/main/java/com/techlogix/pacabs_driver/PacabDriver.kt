package com.techlogix.pacabs_driver

import android.app.Activity
import android.app.Application
import android.content.Context
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverResponseModel
import com.techlogix.pacabs_driver.utility.SharePrefData

class PacabDriver  : Application() {

    private var baseActivity: Activity? = null

    companion object {
        var INSTANCE: PacabDriver? = null
        var loginResponse: CreateDriverResponseModel? = null
        var bookingID:Long=0
        var statusID:Long=0
        var flowID:Long=0
        var context: Context? = null
        var rideStatus=0;

        var mobileNumber="0";
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        context = this
        SharePrefData.getInstance().setContext(applicationContext)
    }

    fun setActivity(baseActivity: Activity) {
        this.baseActivity = baseActivity
    }

    fun getBaseActivity(): Activity? {
        return baseActivity
    }
}