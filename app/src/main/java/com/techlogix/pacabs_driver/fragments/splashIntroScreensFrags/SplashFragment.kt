package com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.navigation.fragment.findNavController
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.activities.DashboardActivity
import com.techlogix.pacabs_driver.activities.RegistrationLoginActivity
import com.techlogix.pacabs_driver.utility.SharePrefData
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {
    var isGo = false;
    var baseActivity: BaseActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()


//        findNavController().navigate(SplashFragmentDirections.actionGotoEnableLocFrag())

    }


    private fun initViews() {
        baseActivity = requireActivity() as BaseActivity
        setupFadeAnimations(keyImg)
    }

    fun setupFadeAnimations(view: View) {

        val fadeInAnimations = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimations.duration = 1000
        fadeInAnimations.startOffset = 800
        view.startAnimation(fadeInAnimations)
        PacabDriver.loginResponse = SharePrefData.getInstance().lastLoginResponse

        fadeInAnimations.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(anim: Animation?) {

                Handler().postDelayed(Runnable {

                    if (!SharePrefData.getInstance().isFirstTime() && !isGo) {
                        findNavController().navigate(SplashFragmentDirections.gotoIntroFragsFragmentActions())
                        isGo = true
                    } else if (PacabDriver.loginResponse != null) {
                        baseActivity?.openActivity(DashboardActivity::class.java, null)
                        requireActivity().finish()
                    } else {
                        baseActivity?.openActivity(RegistrationLoginActivity::class.java, null)
                        requireActivity().finish()
                    }

                }, 1000)

            }

            override fun onAnimationRepeat(anim: Animation?) {
            }

            override fun onAnimationStart(anim: Animation?) {
            }
        })
    }
}