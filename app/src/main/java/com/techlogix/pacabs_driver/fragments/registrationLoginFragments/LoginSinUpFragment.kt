package com.techlogix.pacabs_driver.fragments.registrationLoginFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.adapters.FragmentsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_login_sin_up.*

class LoginSinUpFragment<T> : Fragment() {
    var baseActivity: BaseActivity? = null

    //    val args: LoginSinUpFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_sin_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            initViews(view)
        } catch (e: Exception) {
            Log.e("exx", e.message + "")
        }
    }

    private fun initViews(view: View) {
        baseActivity = activity as BaseActivity?

/*
        for (i in 0..registrationTabLayout.tabCount) {
            val textView =
                LayoutInflater.from(context).inflate(R.layout.custom_tv, null) as TextView
            val typeFace = Typeface.createFromAsset(context?.assets, "fonts/quicksand_regular.otf")
            textView.setTypeface(typeFace)
            registrationTabLayout.getTabAt(i)?.setCustomView(textView)
        }
*/

        registrationTabLayout.setupWithViewPager(registrationViewPager)
        setupViewpage(registrationViewPager)
    }

    private fun setupViewpage(registrationViewPager: ViewPager?) {
        val fragList = arrayListOf<Fragment>()
        val fragTitleList = arrayListOf<String>()
        fragList.add(SignupFragment<T>())
        fragList.add(LoginFragment<T>())
        context?.resources?.getString(R.string.signup)?.let { fragTitleList.add(it) }
        context?.resources?.getString(R.string.login)?.let  { fragTitleList.add(it) }

        val pagerAdapter = context?.let {
            FragmentsViewPagerAdapter(it, childFragmentManager, fragList, fragTitleList)
        }
        registrationViewPager?.adapter = pagerAdapter
    }

}