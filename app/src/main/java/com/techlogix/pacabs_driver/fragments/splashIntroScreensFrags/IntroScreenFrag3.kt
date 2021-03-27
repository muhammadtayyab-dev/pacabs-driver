package com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.techlogix.pacabs_driver.R
import kotlinx.android.synthetic.main.fragment_intro_screen_frag3.*

class IntroScreenFrag3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_screen_frag3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        gotoEnableLocBtn.setOnClickListener {
            findNavController(requireParentFragment()).navigate(IntroMainFragmentDirections.gotoEnabelLocFragmentActions())
        }
    }

}