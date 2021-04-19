package com.techlogix.pacabs_driver.activities

import android.os.Bundle
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapter2
import com.techlogix.pacaps.models.rides.MyRidesModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.activity_my_rides.*

class MyRidesActivity : BaseActivity(), GenericCallback<Any> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rides)
        initViews()

    }

    private fun initViews() {
        backArrowImg.setOnClickListener {
            super.onBackPressed()
        }

        val myRidesArray = arrayListOf<MyRidesModel>()
        myRidesArray.add(MyRidesModel("15-Nov-2020",
            getString(R.string.hundred_),
            "Canclled",
            "Cash",
            getString(R.string.dummu_loc),
            getString(R.string.dummu_loc)))
        myRidesArray.add(MyRidesModel("16-Nov-2020",
            getString(R.string.hundred_),
            "Dropped",
            "PayTm",
            getString(R.string.dummu_loc),
            getString(R.string.dummu_loc)))
        myRidesArray.add(MyRidesModel("17-Nov-2020",
            getString(R.string.hundred_),
            "Dropped",
            "RazorPay",
            getString(R.string.dummu_loc),
            getString(R.string.dummu_loc)))
        myRidesRecycelrView.adapter =
            ActivityGenericAdapter2(Utility.MY_RIDES, myRidesArray, this)

    }

    override fun GenericCallType(T: Any) {
        TODO("Not yet implemented")
    }
}