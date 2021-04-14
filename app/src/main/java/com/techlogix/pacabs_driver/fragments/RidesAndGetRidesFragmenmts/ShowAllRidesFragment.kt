package com.techlogix.pacabs_driver.fragments.RidesAndGetRidesFragmenmts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.models.jobsModels.MyJobsModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_show_all_rides.*
import java.util.ArrayList

class ShowAllRidesFragment<T> : Fragment(), GenericCallback<T> {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_all_rides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        val list = arrayListOf<MyJobsModel>()
        list.add(MyJobsModel(R.drawable.ic_user, "Ali", getString(R.string.dummu_loc), "2KM"))
        list.add(MyJobsModel(R.drawable.ic_user, "Raza", getString(R.string.dummu_loc), "5KM"))
        list.add(MyJobsModel(R.drawable.ic_user, "Rehman", getString(R.string.dummu_loc), "1.5KM"))
        list.add(MyJobsModel(R.drawable.ic_user, "Qaim", getString(R.string.dummu_loc), "8KM"))
        list.add(MyJobsModel(R.drawable.ic_user, "Tayyab", getString(R.string.dummu_loc), "7KM"))
        val jobsAdapter = ActivityGenericAdapte(Utility.MY_JOBS, list as ArrayList<*>, this)
        showAllRidesRecyclerView.adapter = jobsAdapter;
    }

    override fun GenericCallType(T: Any) {
        if (T is Int) {
            (showAllRidesRecyclerView.adapter as ActivityGenericAdapte<T>).list.removeAt(T)
            (showAllRidesRecyclerView.adapter as ActivityGenericAdapte<T>).notifyItemRemoved(T)
        } else if (T is MyJobsModel) {
        findNavController().navigate(ShowAllRidesFragmentDirections.gotoShowInformationFragment())
        }
    }

}