package com.techlogix.pacabs_driver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.models.jobModels.JobResponseModel
import kotlinx.android.synthetic.main.activity_rides_and_get_rides_activty.*

class RidesAndGetRidesActivty<T>   : BaseActivity()/*, APIManager.CallbackGenric<T> */{
    var acceptedResponse: JobResponseModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rides_and_get_rides_activty)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Current Ride")

        try {
            if (intent.extras != null)
                acceptedResponse = intent.extras!!.getSerializable("acceptedResponse") as JobResponseModel?
//            else if (SharePrefData.getInstance().acceptedJobResponse != null)
//                acceptedResponse = SharePrefData.getInstance().acceptedJobResponse
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                resetAllIDs()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
/*
    fun callLoginAgain() {
        APIManager.getInstance()
            .loginDriver(
                LoginDriverRequestModel(
                    HireCabDriver.loginResponse!!.mobile,
                    HireCabDriver.loginResponse!!.password
                ), this
            )
    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {

        if (response!!.result != null && response!!.result is CreateDriverResponseModel) {
            HireCabDriver.loginResponse = response.result as CreateDriverResponseModel
            SharePrefData.getInstance().setLAST_LOGIN_RESPONSE(HireCabDriver.loginResponse)
            resetAllIDs()
            super.onBackPressed()
            finish()
        }
    }*/

}