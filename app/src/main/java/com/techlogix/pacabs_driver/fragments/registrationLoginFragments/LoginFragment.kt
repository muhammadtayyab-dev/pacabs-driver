package com.techlogix.pacabs_driver.fragments.registrationLoginFragments

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.PacabDriver.Companion.mobileNumber
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverResponseModel
import com.techlogix.pacabs_driver.models.createDriverModels.LoginDriverRequestModel
import com.techlogix.pacabs_driver.network.APIManager
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment<T> : Fragment(), APIManager.CallbackGenric<T> {

    var baseActivity: BaseActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        baseActivity = activity as BaseActivity?

        keyImg.setOnClickListener {
            show_password()
        }

        show_password_cb.setOnClickListener {
            show_password()
        }

        loginBtn.setOnClickListener {
            if (validateFeilds())
            {
                mobileNumber = numberEd.text.toString()
                APIManager.getInstance()
                    .loginDriver(
                        LoginDriverRequestModel(
                            numberEd.text.toString(),
                            passwordEd.text.toString()
                        ), this
                    )
            }
        }
    }



    private fun show_password()
    {
        if (passwordEd.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            passwordEd.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            passwordEd.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        passwordEd.setSelection(passwordEd.text.toString().length)
    }

    private fun validateFeilds(): Boolean {
        if (numberEd.text.toString().isEmpty()) {
            numberEd.setError("Please enter mobile number")
            numberEd.requestFocus()
            return false
        } else if (passwordEd.text.toString().isEmpty()) {
            passwordEd.setError("Please enter password")
            passwordEd.requestFocus()
            return false
        } else {
            numberEd.setError(null)
            passwordEd.setError(null)
            return true
        }
        return false
    }


    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result != null && response!!.result is CreateDriverResponseModel) {
            findNavController().navigate(LoginSinUpFragmentDirections.gotoOTPFragmentAction())
/*
            baseActivity?.openActivity(DashboardActivity::class.java, null)
            requireActivity().finish()
*/
        } else {

            (PacabDriver.INSTANCE!!.getBaseActivity() as BaseActivity?)!!.showErrorDialog(
                "Login Error",
                "Invalid User/Password",
                null
            )

        }
    }

}