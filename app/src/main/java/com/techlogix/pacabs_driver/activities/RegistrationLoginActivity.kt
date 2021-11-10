package com.techlogix.pacabs_driver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.utility.SharePrefData

class RegistrationLoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_login)
        SharePrefData.getInstance().setFirstTime(true)

    }
}