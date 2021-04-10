package com.techlogix.pacabs_driver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.customViews.CircleImageView
import com.techlogix.pacabs_driver.models.NavMenuModel
import com.techlogix.pacabs_driver.models.bookingsModels.MyBookingsResponseModel
import com.techlogix.pacabs_driver.models.jobsModels.MyJobsModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility

class ActivityGenericAdapte<T>(
    var type: Int,
    var list: ArrayList<*>,
    var callback: GenericCallback<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (type == Utility.MY_JOBS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_jobs_items_layout, parent, false)
            return MyJobsHolder(view)
        } else if (type == Utility.NAV_ITEMS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.drawer_items_layout, parent, false)
            return MyNavItemsHolder(view)
        } else if (type == Utility.MY_BOOKING) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bookings_items_layout, parent, false)
            return MyBookingsHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_jobs_items_layout, parent, false)
            return MyJobsHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = list.get(holder.adapterPosition)
        if (holder is MyJobsHolder && obj is MyJobsModel) {
            holder.userImg.setImageResource(obj.userImage)
            holder.userNameTv.text = obj.userName
            holder.distanceTv.text = obj.distance
            holder.descTv.text = obj.rideDesc
        } else if (holder is MyNavItemsHolder && obj is NavMenuModel) {
            when (obj.isSelected) {
                true -> holder.rootLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.iconImg.context,
                        R.color.gray_div_dark
                    )
                )

                false -> holder.rootLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.iconImg.context,
                        R.color.transparent
                    )
                )
            }
            holder.iconImg.setImageResource(obj.icon)
            holder.titleTv.text = obj.value
            holder.rootLayout.setOnClickListener {
                callback.GenericCallType(holder.adapterPosition)
                setSelection(obj.value)
            }
        } else if (holder is MyBookingsHolder && obj is MyBookingsResponseModel) {
            holder.bookingUserImg.setImageResource(obj.userImge)
            holder.bookingUserNameTv.text = obj.bookingUserName
            holder.bookingIDTV.text = obj.bookingId
            holder.bookingHrTv.text = obj.bookingHrs
            holder.bookingDateTv.text = obj.bookingDate
            holder.estRupessTv.text = obj.bookingsEST
        }
    }

    fun setSelection(itemToBeSelected: String) {
        for (item in list) {
            if (item is NavMenuModel) {
                item.isSelected = item.value.equals(itemToBeSelected)
            } /*else if (item is CabDetailsInformationModel) {
                item.isSelected = item.cabType.equals(itemToBeSelected)
            }*/
        }
        notifyDataSetChanged()
    }

    class MyJobsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg = itemView.findViewById(R.id.userImg) as CircleImageView
        val distanceTv = itemView.findViewById(R.id.distanceTv) as TextView
        val userNameTv = itemView.findViewById(R.id.userNameTv) as TextView
        val descTv = itemView.findViewById(R.id.descTv) as TextView
        val cancelTv = itemView.findViewById(R.id.cancelTv) as TextView
        val acceptBtn = itemView.findViewById(R.id.acceptBtn) as Button
    }

    class MyNavItemsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImg = itemView.findViewById(R.id.iconImg) as ImageView
        val titleTv = itemView.findViewById(R.id.titleTv) as TextView
        val rootLayout = itemView.findViewById(R.id.rootLayout) as ConstraintLayout
        val div = itemView.findViewById(R.id.div) as View

    }

    class MyBookingsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout = itemView.findViewById(R.id.rootLayout) as ConstraintLayout
        val bookingUserImg = itemView.findViewById(R.id.bookingUserImg) as CircleImageView
        val bookingUserNameTv = itemView.findViewById(R.id.bookingUserNameTv) as TextView
        val bookingIDTV = itemView.findViewById(R.id.bookingIDTV) as TextView
        val bookingHrTv = itemView.findViewById(R.id.bookingHrTv) as TextView
        val bookingDateTv = itemView.findViewById(R.id.bookingDateTv) as TextView
        val estRupessTv = itemView.findViewById(R.id.estRupessTv) as TextView
    }

}