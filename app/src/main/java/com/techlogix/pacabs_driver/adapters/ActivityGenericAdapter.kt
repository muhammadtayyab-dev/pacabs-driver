package com.techlogix.pacabs_driver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.customViews.CircleImageView
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.models.NavMenuModel
import com.techlogix.pacabs_driver.models.jobModels.JobResponseModel
import com.techlogix.pacabs_driver.models.myAllBookingModels.AllBookingResponseModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility

class ActivityGenericAdapte<T>(
    var context: Context,
    var type: Int,
    var list: ArrayList<*>,
    var callback: GenericCallback<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        this.context = context

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
        } else if (type == Utility.MY_WALLET) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_wallet_items_layout, parent, false)
            return MyWalletHolder(view)
        }   else if (type == Utility.EARNING_FILTERS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.earning_filter_items_layout, parent, false)
            return EarningHolder(view)
        } else if (type == Utility.TRIP_STATICS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.trip_statics_items_layout, parent, false)
            return TripStaticsHolder(view)
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
        if (holder is MyJobsHolder && obj is JobResponseModel) {
            holder.userImg.setImageResource(R.drawable.ic_user)
            holder.userNameTv.text = obj.username
            holder.distanceTv.text = "0.0"
            holder.bookingTypeValueTv.text = obj.vehiclecategory

            holder.acceptBtn.setOnClickListener {
                callback.GenericCallType(obj);
            }
            holder.cancelTv.setOnClickListener {
                callback.GenericCallType(holder.adapterPosition)
            }
        }else if (holder is MyNavItemsHolder && obj is NavMenuModel) {
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
        }  else if (holder is MyBookingsHolder && obj is AllBookingResponseModel) {

            if (obj.bookingStatus == StatusIDEnums.Cancelled.statusID.value.toInt() || obj.bookingStatus == 0)
            {
                holder.cancelledByTv.visibility = View.VISIBLE
                holder.cancellationChargesTv.visibility = View.VISIBLE
            } else {
                holder.cancelledByTv.visibility = View.GONE
                holder.cancellationChargesTv.visibility = View.GONE
            }
            holder.bookingUserNameTv.text = obj.customername
            holder.bookingIDTV.text = obj.tempbookingid.toString()
            holder.bookingDateTv.text = obj.pickupDate
            holder.estRupessTv.text =
                obj.amountToBeCollected.toString() + " " + context.getString(R.string.indian_currency)
            holder.cancelledByTv.text = obj.cancelledBy
            holder.cancellationChargesTv.text = obj.cancelledReason.toString()

        }   else if (holder is MyWalletHolder && obj is AllBookingResponseModel) {
            holder.walletUserTv.text = obj.customername
            holder.dateTv.text = obj.pickupDate
            holder.walletRsTv.text = obj.amountToBeCollected.toString() + " " + context.getString(R.string.indian_currency)
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
        val bookingTypeValueTv = itemView.findViewById(R.id.bookingTypeValueTv) as TextView
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
        val bookingDateTv = itemView.findViewById(R.id.bookingDateTv) as TextView
        val estRupessTv = itemView.findViewById(R.id.estRupessTv) as TextView
        val cancelledByTv = itemView.findViewById(R.id.cancelledByTv) as TextView
        val cancellationChargesTv = itemView.findViewById(R.id.cancellationChargesTv) as TextView
    }

    class TripStaticsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingUserImg = itemView.findViewById(R.id.bookingUserImg) as CircleImageView
        val typeTV = itemView.findViewById(R.id.typeTV) as TextView
        val fromTv = itemView.findViewById(R.id.fromTv) as TextView
        val toTv = itemView.findViewById(R.id.toTv) as TextView
        val bookingIdTV = itemView.findViewById(R.id.bookingIdTV) as TextView
        val dateTV = itemView.findViewById(R.id.dateTV) as TextView
        val estimationTV = itemView.findViewById(R.id.estimationTV) as TextView
        val cancelledByTv = itemView.findViewById(R.id.cancelledByTv) as TextView
        val cancellationChargesTv = itemView.findViewById(R.id.cancellationChargesTv) as TextView
    }

    class MyWalletHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout = itemView.findViewById(R.id.rootLayout) as CardView
        val walletUserTv = itemView.findViewById(R.id.walletUserTv) as TextView
        val dateTv = itemView.findViewById(R.id.dateTv) as TextView
        val walletRsTv = itemView.findViewById(R.id.walletRsTv) as TextView

    }
     class EarningHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeSelection = itemView.findViewById(R.id.typeSelection) as TextView
        val viewLine = itemView.findViewById(R.id.viewLine) as View

    }


}