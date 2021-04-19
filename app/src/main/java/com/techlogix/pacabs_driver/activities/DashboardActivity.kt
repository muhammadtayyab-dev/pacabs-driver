package com.techlogix.pacabs_driver.activities

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.techlogix.pacabs_driver.PacabDriver.Companion.context
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.adapters.FragmentsViewPagerAdapter
import com.techlogix.pacabs_driver.customViews.viewpager.SwipeDisabledViewPager
import com.techlogix.pacabs_driver.fragments.dashboardFragments.BookingsFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.DashboardMainFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.ProfileFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.WalletFragment
import com.techlogix.pacabs_driver.models.NavMenuModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.ArrayList

class DashboardActivity<T> : BaseActivity(), View.OnClickListener, GenericCallback<T> {
    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initViews()
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerImg.setOnClickListener(this)
        val navArray = arrayListOf<NavMenuModel>()
        navArray.add(NavMenuModel(R.drawable.booking_white_bg, getString(R.string.bookings), false))
        navArray.add(NavMenuModel(R.drawable.wallet_pink_bg, getString(R.string.wallet), false))
        navArray.add(
            NavMenuModel(
                R.drawable.topup_purple_bg,
                getString(R.string.top_up),
                false
            )
        )
        navArray.add(
            NavMenuModel(
                R.drawable.manage_vehicales_bg,
                getString(R.string.manageVehicles),
                false
            )
        )
        navArray.add(
            NavMenuModel(
                R.drawable.my_rides_bg,
                getString(R.string.my_rides),
                false
            )
        )
        navArray.add(
            NavMenuModel(
                R.drawable.booking_white_bg,
                getString(R.string.my_earminhs),
                false
            )
        )
        navArray.add(NavMenuModel(R.drawable.support_bg, getString(R.string.support), false))
        navItemsRecycler.adapter = ActivityGenericAdapte(
            Utility.NAV_ITEMS,
            navArray as ArrayList<*>, this
        )
        onlineSwitch.setOnCheckedChangeListener { view, isChecked ->
            when (isChecked) {
                true -> {
                    offlineLayout.visibility = View.GONE
                    onlineOfflineTv.text = getString(R.string.online)
                }
                false -> {
                    offlineLayout.visibility = View.VISIBLE
                    onlineOfflineTv.text = getString(R.string.offline)


                }
            }
        }

        tabss.setupWithViewPager(disableSwipeViewPager)
        setupViewPager(disableSwipeViewPager)
    }

    private fun setupViewPager(disableSwipeViewPager: SwipeDisabledViewPager?) {
        val fragsList = arrayListOf<Fragment>()
        val fragsTitleList = arrayListOf<String>()

        fragsList.add(DashboardMainFragment<T>())
        fragsList.add(BookingsFragment<T>())
        fragsList.add(WalletFragment<T>())
        fragsList.add(ProfileFragment<T>())

        fragsTitleList.add(getString(R.string.home))
        fragsTitleList.add(getString(R.string.bookings))
        fragsTitleList.add(getString(R.string.wallet))
        fragsTitleList.add(getString(R.string.profile))

        val fragViewAdapter = context?.let {
            FragmentsViewPagerAdapter(it, supportFragmentManager, fragsList, fragsTitleList)
        }
        disableSwipeViewPager?.adapter = fragViewAdapter
        tabss.getTabAt(0)?.setIcon(R.drawable.home_selector)
        tabss.getTabAt(1)?.setIcon(R.drawable.bookings_selector)
        tabss.getTabAt(2)?.setIcon(R.drawable.wallet_selector)
        tabss.getTabAt(3)?.setIcon(R.drawable.user_selector)
        disableSwipeViewPager?.offscreenPageLimit = 4
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.drawerImg) {
            if (drawerLayout!!.isOpen) {
                drawerLayout!!.closeDrawer(Gravity.LEFT)
            } else {
                drawerLayout?.open()
            }
        }
    }

    override fun GenericCallType(T: Any) {
        drawerLayout?.closeDrawer(Gravity.LEFT)
        Handler().postDelayed(Runnable {
            if (T is Int) {
                if (T == 0) {
                    disableSwipeViewPager.setCurrentItem(1,true)
                }else if(T==1){
                    disableSwipeViewPager.setCurrentItem(2,true)
                }else if(T ==2){
                    openActivity(ETopUpActivity::class.java,null)
                }else if(T==3){
                    openActivity(ManageCabsActivity::class.java,null)
                }else if(T==4){
                    openActivity(MyRidesActivity::class.java,null)
                }
            }
        },1500)

    }
}