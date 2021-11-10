package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.bookingsModels.MyBookingsResponseModel
import com.techlogix.pacabs_driver.models.myAllBookingModels.AllBookingResponseModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_bookings.*

class BookingsFragment<T> : Fragment(), GenericCallback<T>, APIManager.CallbackGenric<T> {

    val allBookingsList = arrayListOf<AllBookingResponseModel>()
    val sortedBookingsList = arrayListOf<AllBookingResponseModel>()
    var itemPos=1;

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
        //by default
        getAllBookings()

        completedBookingLayout.setOnClickListener {
            itemPos=1
            setSelector(itemPos)
        }
        upcomingBookingLayout.setOnClickListener {
            itemPos=2
            setSelector(itemPos)
        }
        cancelledBookingLayout.setOnClickListener {
            itemPos=3
            setSelector(itemPos)
        }

        refreshBookings.setOnRefreshListener {
            getAllBookings()
        }

        refreshBookingBtn.setOnClickListener {
            getAllBookings()
        }
    }

    private fun getAllBookings() {
        APIManager.getInstance()
            .getAllMyBookings(
                PacabDriver.loginResponse!!.id.toString(),
                this
            )
    }

    private fun setSelector(i: Int)
    {
        if (i == 1) {
            completedBookingLayout.isSelected = true
            upcomingBookingLayout.isSelected = false
            cancelledBookingLayout.isSelected = false
            sort(StatusIDEnums.Completed_Ride.statusID.value.toInt());
        } else if (i == 2) {
            completedBookingLayout.isSelected = false
            upcomingBookingLayout.isSelected = true
            cancelledBookingLayout.isSelected = false
            sort(StatusIDEnums.Assigned.statusID.value.toInt());
        } else if (i == 3) {
            completedBookingLayout.isSelected = false
            upcomingBookingLayout.isSelected = false
            cancelledBookingLayout.isSelected = true
            sort(StatusIDEnums.Cancelled.statusID.value.toInt());
        }
    }

    override fun GenericCallType(T: Any) {
    }

    fun sort(type: Int) {
        if(allBookingsList.size>0)
        {

            if (type == StatusIDEnums.Completed_Ride.statusID.value.toInt()) {
                sortedBookingsList.clear()
                for (i in 0..allBookingsList.size-1) {
                    if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Completed_Ride.statusID.value.toInt()) {
                        sortedBookingsList.add(allBookingsList.get(i))
                    }
                }
                completedBookingCount.text=sortedBookingsList.size.toString()
            }
            if (type == StatusIDEnums.Assigned.statusID.value.toInt()) {
                sortedBookingsList.clear()
                for (i in 0..allBookingsList.size-1) {
                    if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Assigned.statusID.value.toInt()) {
                        sortedBookingsList.add(allBookingsList.get(i))
                    }
                }
                upComingBookingCount.text=sortedBookingsList.size.toString()
            }
            if (type == StatusIDEnums.Cancelled.statusID.value.toInt()) {
                sortedBookingsList.clear()
                for (i in 0..allBookingsList.size-1) {
                    if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Cancelled.statusID.value.toInt()
                        ||
                        allBookingsList.get(i).bookingStatus == 0 )
                    {
                        sortedBookingsList.add(allBookingsList.get(i))
                    }
                }
                cancelBookingCount.text=sortedBookingsList.size.toString()
            }

            if (sortedBookingsList.size<=0)
            {
                bookingsRecyclerView.visibility=View.GONE
                noData.visibility=View.VISIBLE
            }
            else {
                bookingsRecyclerView.visibility=View.VISIBLE
                noData.visibility=View.GONE
                bookingsRecyclerView.adapter =
                    ActivityGenericAdapte<T>(
                        requireContext(),
                        Utility.MY_BOOKING,
                        sortedBookingsList,
                        this
                    )
                (bookingsRecyclerView.adapter as ActivityGenericAdapte<T>).notifyDataSetChanged()
            }
        }
        else
        {
            noData.visibility=View.VISIBLE
        }

    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result != null) {
//            adapter.notifyDataSetChanged()

            refreshBookings.isRefreshing = false
            allBookingsList.clear()

            allBookingsList.addAll(response.result as ArrayList<AllBookingResponseModel>)
            /*      for (i in 0..allBookingsList.size) {
                      if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Completed_Ride.statusID.value.toInt()) {
                          completedBookingsList.add(allBookingsList.get(i))
                      } else if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Completed_Ride.statusID.value.toInt()) {
                          upcomingBookingsList.add(allBookingsList.get(i))
                      } else if (allBookingsList.get(i).bookingStatus == StatusIDEnums.Cancelled.statusID.value.toInt()) {
                          cancelledBookingsList.add(allBookingsList.get(i))
                      }
                  }*/
            setSelector(itemPos)
        }
    }
}