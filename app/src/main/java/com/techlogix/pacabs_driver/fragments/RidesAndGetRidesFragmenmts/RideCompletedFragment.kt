package com.techlogix.pacabs_driver.fragments.RidesAndGetRidesFragmenmts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs


import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.eventsModel.UpdateEventRequestModel
import com.techlogix.pacabs_driver.models.walletsModel.WalletResponseModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_ride_completed.*


class RideCompletedFragment<T> : Fragment(), APIManager.CallbackGenric<T> {

    val args: RideCompletedFragmentArgs by navArgs()
    var baseActivity: BaseActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {

        baseActivity = requireActivity() as BaseActivity
        requireActivity().setTitle("Ride Summary")

        try {
            estValueTv.text = args.endRideResponse!!.estimatedCost
            distanceValueTv.text = args.endRideResponse!!.dist.toString()
            fareValueTv.text = args.endRideResponse!!.fare.toString()
            totalValueTv.text = args.endRideResponse!!.maxCost

            bookingId.text = "Booking ID: " + PacabDriver.bookingID
            startDestTv.text = "Start: " + args.endRideResponse.endDest
            endDestTv.text = "End: " + args.endRideResponse.startDest

            rideEndedTv.setOnClickListener {
//                resetData()
                getWallet()
            }

            updateEventStatusCall()
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

    }

    fun updateEventStatusCall() {
        APIManager.getInstance().updateEvent(
            UpdateEventRequestModel(
                PacabDriver.bookingID,
                PacabDriver.loginResponse!!.vehicleid,
                Utility.currentUserLatLng!!.latitude,
                Utility.currentUserLatLng!!.longitude,
                StatusIDEnums.Completed_Ride.statusID.value
            ), this
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // clear all stored ID's now, coz ride is completed
        baseActivity!!.resetAllIDs()
        getWallet()
    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result is WalletResponseModel) {
            PacabDriver.loginResponse!!.walletbalance =
                (response.result as WalletResponseModel).walletBalance
            resetData()
        }
    }

    fun resetData() {
        baseActivity!!.resetAllIDs()
        var activity = getActivity();
        if (activity != null) {
            activity.finish()
        }
    }


    fun getWallet() {
        APIManager.getInstance()
            .getWalletBalance(
                PacabDriver.loginResponse!!.id.toString(),
                this
            )
    }
}