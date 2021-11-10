package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.myAllBookingModels.AllBookingResponseModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment<T> : Fragment(), GenericCallback<T>, APIManager.CallbackGenric<T> {

    val allBookingsList = arrayListOf<AllBookingResponseModel>()
    val sortedBookingsList = arrayListOf<AllBookingResponseModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {

         getAllBookings()
        refreshWallet.setOnRefreshListener {
            getAllBookings()
        }
        refreshWalletBtn
            .setOnClickListener {
                getAllBookings()
            }
    }

    override fun onResume() {
        super.onResume()
        balanceTv.text =
            getString(R.string.indian_currency) + " " + PacabDriver.loginResponse!!.walletbalance.toString()
    }


    fun getAllBookings() {
        APIManager.getInstance()
            .getAllMyBookings(
                PacabDriver.loginResponse!!.id.toString(),
                this
            )
    }


    override fun GenericCallType(T: Any) {
        TODO("Not yet implemented")
    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result != null) {
            allBookingsList.clear()
            sortedBookingsList.clear()
            refreshWallet.isRefreshing = false

            allBookingsList.addAll(response.result as ArrayList<AllBookingResponseModel>)
            for (i in 0..allBookingsList.size - 1) {
                if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Completed_Ride.statusID.value.toInt()) {
                    sortedBookingsList.add(allBookingsList.get(i))
                }
            }


            if (sortedBookingsList.size <= 0) {
                noData.visibility = View.VISIBLE
                myWalletRecyclerView.visibility = View.GONE
            } else {
                noData.visibility = View.GONE
                myWalletRecyclerView.visibility = View.VISIBLE
                myWalletRecyclerView.adapter =
                    ActivityGenericAdapte<T>(
                        requireContext(),
                        Utility.MY_WALLET,
                        sortedBookingsList,
                        this
                    )
            }
        }
    }
}