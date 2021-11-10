package com.techlogix.pacabs_driver.activities

import `in`.teramatrix.utilities.service.LocationHandler
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.LatLng
 import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.PacabDriver.Companion.context
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.adapters.FragmentsViewPagerAdapter
import com.techlogix.pacabs_driver.customViews.viewpager.SwipeDisabledViewPager
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.fragments.dashboardFragments.BookingsFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.DashboardMainFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.ProfileFragment
import com.techlogix.pacabs_driver.fragments.dashboardFragments.WalletFragment
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.NavMenuModel
import com.techlogix.pacabs_driver.models.jobModels.JobResponseModel
import com.techlogix.pacabs_driver.models.polling.PollingRequestModel
import com.techlogix.pacabs_driver.models.polling.PollingResponseModel
import com.techlogix.pacabs_driver.models.tokenModel.TokenRequestModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacabs_driver.utility.PermissionUtils
import com.techlogix.pacabs_driver.utility.SharePrefData
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import com.techlogix.pacaps.utility.Utility.Companion.UPDATE_AVAILABILITY
import com.techlogix.pacaps.utility.Utility.Companion.getAccuracy
import com.techlogix.pacaps.utility.Utility.Companion.getCurrentDateTime
import com.techlogix.pacaps.utility.Utility.Companion.getSpeed
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*
import java.util.*

class DashboardActivity<T> : BaseActivity(), View.OnClickListener, GenericCallback<T>,
    APIManager.CallbackGenric<T> {
    var drawerLayout: DrawerLayout? = null
    var isOnline: Boolean? = false
    var fragViewAdapter: FragmentsViewPagerAdapter? = null
    var isJobRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

/*
        PacabDriver.bookingID = SharePrefData.getInstance().bookinG_ID
        PacabDriver.statusID = SharePrefData.getInstance().statuS_ID
        PacabDriver.flowID = SharePrefData.getInstance().floW_ID
*/

        PacabDriver.rideStatus = SharePrefData.getInstance().locaL_RIDE_STATUS.toInt()

        LocationHandler(context).setLocationListener(LocationListener {
            // Get the best known location
            Utility.currentUserLatLng = LatLng(it.latitude, it.longitude)

            if (Utility.currentLocation == null)
                Utility.previousLocation = it
            else
                Utility.previousLocation = Utility.currentLocation

            Utility.currentLocation = it

        }).start()

        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            initViews()
        } else {
            PermissionUtils.requestLocationPermissions(this, Utility.LOCATION_PERMISSIONS_CODE)
        }

    }

    private fun initViews() {


        drawerLayout = findViewById(R.id.drawerLayout);
        drawerImg.setOnClickListener(this)

        user_name_tv_2.setText(PacabDriver.loginResponse?.name)
        phone_number.setText(PacabDriver.loginResponse?.mobile)

        val navArray = arrayListOf<NavMenuModel>()
        navArray.add(NavMenuModel(R.drawable.booking_white_bg, getString(R.string.bookings), false))
        navArray.add(NavMenuModel(R.drawable.wallet_gray_bg, getString(R.string.wallet), false))
        navArray.add(
            NavMenuModel(
                R.drawable.booking_trip_statistics,
                getString(R.string.top_up),
                false
            )
        )
     /*   navArray.add(
            NavMenuModel(
                R.drawable.manage_vehicales_bg,
                getString(R.string.manageVehicles),
                false
            )
        )*/
        navArray.add(
            NavMenuModel(
                R.drawable.currency_mehron_bg,
                getString(R.string.my_earminhs),
                false
            )
        )
        navArray.add(
            NavMenuModel(
                R.drawable.customer_support_purple_bg,
                getString(R.string.contact_support),
                false
            )
        )
        navArray.add(
            NavMenuModel(
                R.drawable.ic_circle_signout,
                getString(R.string.logout),
                false
            )
        )

        navItemsRecycler.adapter = ActivityGenericAdapte(
            this,
            Utility.NAV_ITEMS,
            navArray as ArrayList<*>, this
        )

        tabss.setupWithViewPager(disableSwipeViewPager)
        setupViewPager(disableSwipeViewPager)


        updateToken()
        getCurrentJobRequest() // need to run Current Job Request early before JobPolling
        setData()



        Log.d("actflow", "oncreate")

        onlineSwitch.setOnCheckedChangeListener { view, isChecked ->
            when (isChecked) {
                true -> {
                    SetStatusExtras(true)
                    callAvailablity(1)

                    Handler().removeCallbacks(runnablePolling) // stop polling
                    Handler().removeCallbacks(runnableJob) // stop jobRequest

                    runnablePolling.run() // run polling api call
                    if (PacabDriver.statusID < StatusIDEnums.Assigned.statusID.value) { // if status id is <5 means, no job is accepted
                        isJobRunning = true
                        runnableJob.run() // run polling api call
                    }

                }
                false -> {
                    SetStatusExtras(false)
                    callAvailablity(0)
                    isJobRunning = true
                    Handler().removeCallbacks(runnablePolling) // stop polling
                    Handler().removeCallbacks(runnableJob) // stop jobRequest
                }
            }
        }


        /*   if (PacabDriver.statusID >= StatusIDEnums.Assigned.statusID.value) // means when activity Created, check if status id is >= 5 means ride is assigned, so go to Ride Activity
           {
               val myIntent = Intent(this, RidesAndGetRidesActivty::class.java)
               myIntent.putExtra("acceptedResponse", SharePrefData.getInstance().acceptedJobResponse)
               myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
               this.startActivity(myIntent)
           }*/
    }


    fun setData() {
        if (SharePrefData.getInstance().isOnline_Oflline) {
            onlineSwitch.isChecked = true
            SetStatusExtras(true)

            runnablePolling.run() // run polling api call

            if (PacabDriver.statusID < StatusIDEnums.Assigned.statusID.value) { // if status id is <5 means, no job is accepted
                isJobRunning = true
                runnableJob.run() // run polling api call
            }

        } else {
            onlineSwitch.isChecked = false
            SetStatusExtras(false)
            isJobRunning = true
            Handler().removeCallbacks(runnablePolling) // stop polling
            Handler().removeCallbacks(runnableJob) // stop jobRequest

        }
    }


    val runnablePolling = Runnable {
        getPolling()
    }
    val runnableJob = Runnable {
        getJobRequest()
    }

    fun getPolling() {
        APIManager.getInstance()
            .polling(
                PollingRequestModel(
                    PacabDriver.loginResponse!!.vehicleid.toString(),
                    Utility.currentUserLatLng!!.latitude,
                    Utility.currentUserLatLng!!.longitude,
                    getSpeed(),
                    1,
                    getAccuracy(),
                    getCurrentDateTime(),
                    PacabDriver.bookingID
                ), this
            )
    }

    fun getJobRequest() {
        APIManager.getInstance()
            .jobRequest(
                PacabDriver.loginResponse!!.vehicleid.toString(),
                this
            )
    }

    fun getCurrentJobRequest() {
        APIManager.getInstance()
            .getCurrentJob(
                PacabDriver.loginResponse!!.vehicleid.toString(),
                this
            )
    }

    fun updateToken() {
        APIManager.getInstance()
            .updateToken(
                TokenRequestModel(
                    PacabDriver.loginResponse!!.id,
                    SharePrefData.getInstance().userToken
                ),
                this
            )
    }

    fun SetStatusExtras(boolean: Boolean) {
        if (boolean) {
            isOnline = boolean
            SharePrefData.getInstance().isOnline_Oflline = boolean
            offlineLayout.visibility = View.GONE
            onlineOfflineTv.text = getString(R.string.online)
            disableSwipeViewPager?.setCurrentItem(0)
        } else {
            isOnline = boolean
            SharePrefData.getInstance().isOnline_Oflline = boolean
            offlineLayout.visibility = View.VISIBLE
            onlineOfflineTv.text = getString(R.string.offline)
            disableSwipeViewPager?.setCurrentItem(3)
        }
    }

    fun callAvailablity(status: Int) {
        APIManager.getInstance()
            .updateAvailability(PacabDriver.loginResponse!!.vehicleid.toString(), status, this)
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

        fragViewAdapter = context?.let {
            FragmentsViewPagerAdapter(it, supportFragmentManager, fragsList, fragsTitleList)
        }
        disableSwipeViewPager?.adapter = fragViewAdapter
        tabss.getTabAt(0)?.setIcon(R.drawable.home_selector)
        tabss.getTabAt(1)?.setIcon(R.drawable.bookings_selector)
        tabss.getTabAt(2)?.setIcon(R.drawable.wallet_selector)
        tabss.getTabAt(3)?.setIcon(R.drawable.user_selector)

        disableSwipeViewPager?.offscreenPageLimit = 4
//        disableSwipeViewPager?.setCurrentItem(3)
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
                    disableSwipeViewPager.setCurrentItem(1, true)
                } else if (T == 1) {
                    disableSwipeViewPager.setCurrentItem(2, true)
                } else if (T == 2) {
                    openActivity(ETopUpActivity::class.java, null)
                }/* else if (T == 3) {
                    openActivity(ManageCabsActivity::class.java, null)
                } */else if (T == 3) {
                    openActivity(EarningsActivity::class.java, null)
                } else if (T == 4) {
                    openActivity(SupportActivity::class.java, null)
                } else if (T == 5) {
                    SharePrefData.getInstance().setLAST_LOGIN_RESPONSE(null)
                    openActivity(RegistrationLoginActivity::class.java, null)
                    Handler().removeCallbacks(runnablePolling) // stop polling
                    Handler().removeCallbacks(runnableJob) // stop jobRequest
                    finish()
                }
            }
        }, 500)

    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.status == true && requestCode == UPDATE_AVAILABILITY) {
            //do if need something
        } else if (response.result is PollingResponseModel) {
            updateStatusID((response.result as PollingResponseModel).statusid)
            if (this.isOnline!!) {
                Handler().postDelayed(runnablePolling, 12000)
            }


        } else if (requestCode == Utility.JOB_REQUEST) { //run jobRequest on response
            if (response.result == null && this.isOnline!!) {
                isJobRunning = true
                Handler().postDelayed(runnableJob, 8000)
            } else if (response.result != null && response.result is JobResponseModel) {
                updateBookingID((response.result as JobResponseModel).bookingid.toLong())
                updateFlowID((response.result as JobResponseModel).id.toLong())

                val currentFrag = fragViewAdapter?.getItem(0)
                (currentFrag as DashboardMainFragment<T>).onResult(response, requestCode)
            }

        } else if (requestCode == Utility.CURRENT_JOB_REQUEST) {
            if (response.result != null) {

//              var cureentJobResponse: JobResponseModel = (response.result as ArrayList<JobResponseModel>).get(0)
                var cureentJobResponse: JobResponseModel = (response.result as JobResponseModel)

                updateBookingID(cureentJobResponse.bookingid.toLong())
                updateFlowID(cureentJobResponse.id.toLong())
                updateStatusID(cureentJobResponse.bookingStatus.toLong())

                if (PacabDriver.statusID >= StatusIDEnums.Assigned.statusID.value) // means when activity Created, check if status id is >= 5 means ride is assigned, so go to Ride Activity
                {
                    val myIntent = Intent(this, RidesAndGetRidesActivty::class.java)
                    myIntent.putExtra(
                        "acceptedResponse",
                        cureentJobResponse
                    )
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    this.startActivity(myIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (PacabDriver.bookingID == 0L && PacabDriver.statusID == 0L) {
            if (!isJobRunning) // if job handler is running stop and no booking ID have, then start again
            {
                isJobRunning = true
                runnableJob.run()
            }
            Log.d("actflow", "onresume")
            val currentFrag = fragViewAdapter?.getItem(0)
            (currentFrag as DashboardMainFragment<T>).clearData()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utility.LOCATION_PERMISSIONS_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViews()
            } else {
                showToast("Permission not granted")
            }
        }
    }
}