package com.techlogix.pacabs_driver.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapter2
import com.techlogix.pacabs_driver.models.manageCabsModels.MangeCabsResponseModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.activity_manage_cabs.*

class ManageCabsActivity<T> : BaseActivity(), GenericCallback<T> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_cabs)
        initViews()
    }

    private fun initViews() {
        val arrayList = arrayListOf<MangeCabsResponseModel>()
        arrayList.add(
            MangeCabsResponseModel(
                "", "SUV", "5465KKJH564"
                , "Vijay", "ON", "878676565776", true
            )
        )
        arrayList.add(
            MangeCabsResponseModel(
                "", "MUV", "5465KKJH564"
                , "Vijay", "OFF", "878676565776", false
            )
        )
        arrayList.add(
            MangeCabsResponseModel(
                "", "SEDAN", "5465KKJH564"
                , "Vijay", "ON", "878676565776", true
            )
        )
        arrayList.add(
            MangeCabsResponseModel(
                "", "SEDAN", "5465KKJH564"
                , "Vijay", "ON", "878676565776", true
            )
        )
        manageCabsRecyclerView.adapter = ActivityGenericAdapter2<T>(
            Utility.MANAGE_CABS
            , arrayList, this
        )
        backArrowImg.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun GenericCallType(T: Any) {
    }
}