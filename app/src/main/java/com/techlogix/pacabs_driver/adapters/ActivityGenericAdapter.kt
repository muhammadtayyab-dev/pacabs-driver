package com.techlogix.pacabs_driver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.customViews.CircleImageView
import com.techlogix.pacabs_driver.models.jobsModels.MyJobsModel
import com.techlogix.pacaps.utility.Utility

class ActivityGenericAdapte<A>(var type: Int, var list: ArrayList<A>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (type == Utility.MY_JOBS) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_jobs_items_layout, parent, false)
            return MyJobsHolder(view)
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
        }
    }

    class MyJobsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg = itemView.findViewById(R.id.userImg) as CircleImageView
        val distanceTv = itemView.findViewById(R.id.distanceTv) as TextView
        val userNameTv = itemView.findViewById(R.id.userNameTv) as TextView
        val descTv = itemView.findViewById(R.id.descTv) as TextView
        val cancelTv = itemView.findViewById(R.id.cancelTv) as TextView
        val acceptBtn = itemView.findViewById(R.id.acceptBtn) as Button
    }

}