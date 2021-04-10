package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.models.walletsModel.MyWalletResponseModel
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment<T> : Fragment(), GenericCallback<T> {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        val list = arrayListOf<MyWalletResponseModel>()
        list.add(
            MyWalletResponseModel(
                "Ravi", requireContext().getString(R.string.dummy_date),
                requireContext().getString(R.string.hundred_)
            )
        )
        list.add(
            MyWalletResponseModel(
                "VaraParsad", requireContext().getString(R.string.dummy_date),
                requireContext().getString(R.string.hundred_)
            )
        )
        list.add(
            MyWalletResponseModel(
                "Shravan", requireContext().getString(R.string.dummy_date),
                requireContext().getString(R.string.hundred_)
            )
        )
        myWalletRecyclerView.adapter=ActivityGenericAdapte<T>(Utility.MY_WALLET,list,this)
    }

    override fun GenericCallType(T: Any) {
        TODO("Not yet implemented")
    }
}