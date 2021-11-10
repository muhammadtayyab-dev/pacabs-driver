package com.techlogix.pacabs_driver.fragments.registrationLoginFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.PacabDriver.Companion.INSTANCE
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.activities.DashboardActivity
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverResponseModel
import com.techlogix.pacabs_driver.models.createDriverModels.VerifyOTPRequestModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacabs_driver.utility.SharePrefData
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

        continoueBtn.setOnClickListener {
            if (validateOtp()) {
                APIManager.getInstance()
                    .verifyOtp(
                        VerifyOTPRequestModel(
                            firstPinView.text.toString(),
                            PacabDriver.mobileNumber
                        ),
                        this
                    )
            }
        }
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
        if (response?.result != false && response!!.result != null && response!!.result is CreateDriverResponseModel) {
            PacabDriver.loginResponse = response.result as CreateDriverResponseModel
            SharePrefData.getInstance().setLAST_LOGIN_RESPONSE(PacabDriver.loginResponse)

            baseActivity?.openActivity(DashboardActivity::class.java, null)
            requireActivity().finish()
        } else {
            (INSTANCE!!.getBaseActivity() as BaseActivity?)!!.showErrorDialog(
                "Verify OTP",
                "Invalid OTP, please enter valid OTP",
                null
            )
        }
    }

}