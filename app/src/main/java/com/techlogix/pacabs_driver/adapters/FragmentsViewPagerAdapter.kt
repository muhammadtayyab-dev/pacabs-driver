package com.techlogix.pacabs_driver.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*
import kotlin.collections.ArrayList

public class FragmentsViewPagerAdapter(
    context: Context,
    fm: FragmentManager?,
    fragmentList: ArrayList<Fragment>,
    fragTitle: ArrayList<String>
) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var fragmentList: ArrayList<Fragment> =
        ArrayList()
    var fragTitle: ArrayList<String> =
        ArrayList()

    fun addFragments(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragTitle.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return if (fragmentList.size > position && position >= 0) {
            fragmentList[position]
        } else fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragTitle[position]
    }

    init {
        this.fragmentList = fragmentList
        this.fragTitle = fragTitle
    }
}