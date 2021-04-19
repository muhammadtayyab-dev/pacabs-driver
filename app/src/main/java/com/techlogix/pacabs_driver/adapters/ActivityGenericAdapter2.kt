package com.techlogix.pacabs_driver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.models.manageCabsModels.MangeCabsResponseModel
import com.techlogix.pacaps.models.rides.MyRidesModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import com.work.mynotepad.adapters.CustomSpinnerAdapter
import java.util.*
import kotlin.collections.ArrayList

class ActivityGenericAdapter2<T>(
    var type: Int,
    var list: ArrayList<*>,
    var callback: GenericCallback<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (type == Utility.MANAGE_CABS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mange_cabs_items_layout, parent, false)
            return MyMangeCabsHolder(view)
        } else if (type == Utility.MY_RIDES) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_rides_item_layout, parent, false)
            return MyRidesHolder(view);
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_jobs_items_layout, parent, false)
            return MyMangeCabsHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = list.get(holder.adapterPosition)
        if (obj is MangeCabsResponseModel && holder is MyMangeCabsHolder) {
            holder.vehicalNumberTv.text = obj.vehicalNum
            holder.cabNameTv.text = obj.cabName
            holder.cabRunnerNameTv.text = obj.vehicalDriverName
            holder.canRunnerNumberTv.text = obj.vehicalDriverNum
            holder.cabRunnerOnlineTv.text = obj.vehicalDriverStatus
            holder.setSpinnerData(obj.cabStatus)
        } else if (holder is MyRidesHolder && obj is MyRidesModel) {
            holder.timeDateTv.text = obj.data
            holder.rideStatusTv.text = obj.rideStatus
            holder.paymentTv.text = obj.totalPayment
            holder.paymentTypeTv.text = obj.paymentType
            holder.startAddressTv.text = obj.startingAddress
            holder.endAddressTv.text = obj.endingAddress
        }
    }

    class MyMangeCabsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cabType = itemView.findViewById(R.id.cabType) as ImageView
        val cabNameTv = itemView.findViewById(R.id.cabNameTv) as TextView
        val vehicalNumberTv = itemView.findViewById(R.id.vehicalNumberTv) as TextView
        val onOffSpinner = itemView.findViewById(R.id.onOffSpinner) as Spinner
        val cabRunnerNameTv = itemView.findViewById(R.id.cabRunnerNameTv) as TextView
        val cabRunnerOnlineTv = itemView.findViewById(R.id.cabRunnerOnlineTv) as TextView
        val canRunnerNumberTv = itemView.findViewById(R.id.canRunnerNumberTv) as TextView
        val cabRunnerOnHireTv = itemView.findViewById(R.id.cabRunnerOnHireTv) as TextView
        fun setSpinnerData( cabStatus:Boolean) {
            val list= arrayListOf<String>()
            list.add("ON")
            list.add("OFF")
            val spinnerAdapter = CustomSpinnerAdapter(
                onOffSpinner.context,
                R.id.spinner_item_tv,
                R.id.spinner_item_tv,
                list
            )
            onOffSpinner.adapter = spinnerAdapter
            if(cabStatus)
                onOffSpinner.setSelection(0)
            else
                onOffSpinner.setSelection(1)
        }
    }


    class MyRidesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeDateTv = itemView.findViewById(R.id.timeDateTv) as TextView
        val paymentTv = itemView.findViewById(R.id.paymentTv) as TextView
        val rideStatusTv = itemView.findViewById(R.id.rideStatusTv) as TextView
        val paymentTypeTv = itemView.findViewById(R.id.paymentTypeTv) as TextView
        val startAddressTv = itemView.findViewById(R.id.startAddressTv) as TextView
        val endAddressTv = itemView.findViewById(R.id.endAddressTv) as TextView

    }
}