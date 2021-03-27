package com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.FragmentsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_intro_main.*

class IntroMainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        introViewPager.offscreenPageLimit = 3
        dotsTabsLL.setupWithViewPager(introViewPager)
        setupViewPager(introViewPager)
        skipBtn.setOnClickListener {
            introViewPager.setCurrentItem(2, true)
        }

    }

    private fun setupViewPager(introViewPager: ViewPager?) {
        val introFragsArray = arrayListOf<Fragment>()
        introFragsArray.add(IntroScreenFrag1())
        introFragsArray.add(IntroScreenFrag2())
        introFragsArray.add(IntroScreenFrag3())
        val fragsTitlesArray = arrayListOf<String>()
        fragsTitlesArray.add("")
        fragsTitlesArray.add("")
        fragsTitlesArray.add("")
        val adapter = FragmentsViewPagerAdapter(
            requireContext(),
            childFragmentManager,
            introFragsArray,
            fragsTitlesArray
        )
        introViewPager?.adapter = adapter
        introViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    skipBtn.text = ""
                } else {
                    skipBtn.text = requireActivity().getString(R.string.skip)
                }
            }
        })
    }
}