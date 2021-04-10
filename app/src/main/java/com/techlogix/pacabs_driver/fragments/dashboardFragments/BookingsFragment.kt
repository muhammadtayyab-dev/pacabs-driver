package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.models.bookingsModels.MyBookingsResponseModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_bookings.*

class BookingsFragment<T> : Fragment(), GenericCallback<T> {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        val bookingsList = arrayListOf<MyBookingsResponseModel>()
        bookingsList.add(
            MyBookingsResponseModel(
                R.drawable.ic_user, "Raja", "BookingId: 12345",
                "BookingHr: 2Hr", "BookingDate: 3-2-2021", "BookingEst:  ₹2000"
            )
        )
        bookingsList.add(
            MyBookingsResponseModel(
                R.drawable.ic_user, "RajKomar", "BookingId: 12345",
                "BookingHr: 2Hr", "BookingDate: 3-2-2021", "BookingEst:  ₹2000"
            )
        )
        bookingsList.add(
            MyBookingsResponseModel(
                R.drawable.ic_user, "Rahul", "BookingId: 12345",
                "BookingHr: 2Hr", "BookingDate: 3-2-2021", "BookingEst:  ₹2000"
            )
        )
        bookingsList.add(
            MyBookingsResponseModel(
                R.drawable.ic_user, "Vijay", "BookingId: 12345",
                "BookingHr: 2Hr", "BookingDate: 3-2-2021", "BookingEst:  ₹2000"
            )
        )
        bookingsRecyclerView.adapter =
            ActivityGenericAdapte<T>(Utility.MY_BOOKING, bookingsList, this)
    }

    override fun GenericCallType(T: Any) {
    }
}