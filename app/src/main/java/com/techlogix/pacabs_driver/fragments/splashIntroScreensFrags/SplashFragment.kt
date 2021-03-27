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
import com.techlogix.pacabs_driver.R
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {
    var isGo = false;
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
        setupFadeAnimations(keyImg)
        setupFadeAnimations(appNameTv)
    }

    fun setupFadeAnimations(view: View) {

        val fadeInAnimations = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimations.duration = 1000
        fadeInAnimations.startOffset = 800
        view.startAnimation(fadeInAnimations)

        fadeInAnimations.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(anim: Animation?) {

                Handler().postDelayed(Runnable {
                    if (!isGo) {
                        findNavController().navigate(SplashFragmentDirections.gotoIntroFragsFragmentActions())
                        isGo = true
                    }
                }, 2000)

            }

            override fun onAnimationRepeat(anim: Animation?) {
            }

            override fun onAnimationStart(anim: Animation?) {
            }
        })
    }
}