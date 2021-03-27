package com.techlogix.pacabs_driver.fragments.registrationLoginFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.network.APIManager
import kotlinx.android.synthetic.main.fragment_o_t_p.*

class OTPFragment<T> : Fragment(), APIManager.CallbackGenric<T> {
    var baseActivity: BaseActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_o_t_p, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        baseActivity = activity as BaseActivity?
//        if (APIManager.instance.responseModel != null) {
//            firstPinView.setText((APIManager.instance.responseModel.result as CreateUserResponseModel).oTP)
//            userNumberTv.setText((APIManager.instance.responseModel.result as CreateUserResponseModel).mobile)
//        }
//        continoueBtn.setOnClickListener {
//            if (validateOtp()) {
//                val response = APIManager.instance.responseModel.result
//                if (response != null && response is CreateUserResponseModel) {
//                    APIManager.getInstance()
//                        .verifyOtp(VerifyUserOtp(response.mobile, firstPinView.text.toString()),
//                            this)
//                }
//            }
//
//        }

    }

    private fun validateOtp(): Boolean {
        if (firstPinView.length() == 0) {
            baseActivity?.showToast("Please enter otp")
            return false
        } else {
            return true
        }

    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        /*    if (response?.result is CreateUserResponseModel) {
                baseActivity?.openActivity(DashboardActivity::class.java, null)
            }*/
    }

}