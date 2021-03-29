package com.techlogix.pacabs_driver.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.NavHostFragment
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags.EnableLocationFragment
import com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags.IntroMainFragment
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFullScreen()
        setContentView(R.layout.activity_splash)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utility.LOCATION_PERMISSIONS_CODE) {
            val fragment =
                (supportFragmentManager.fragments[0] as NavHostFragment).childFragmentManager.fragments.get(
                    0
                )
            if (fragment is EnableLocationFragment) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }

        }
    }

    override fun onBackPressed() {
        val fragment =
            (supportFragmentManager.fragments[0] as NavHostFragment).childFragmentManager.fragments.get(
                0
            )
        if (fragment is IntroMainFragment || fragment is EnableLocationFragment)
            finish()
        else
            super.onBackPressed()
    }


}