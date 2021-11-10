package com.techlogix.pacabs_driver.fragments.RidesAndGetRidesFragmenmts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techlogix.pacabs_driver.R
import kotlinx.android.synthetic.main.fragment_ride_information.*

class RideInformationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        requireActivity().setTitle("#12345")
/*
        gotoPickup.setOnClickListener {
            findNavController().navigate(RideInformationFragmentDirections.gotoPickupFragment())
        }
*/
    }
}