package com.techlogix.pacabs_driver.fragments.registrationLoginFragments

import `in`.teramatrix.utilities.service.LocationHandler
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.LatLng
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.citiesModels.CititesResponseModel
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverRequestModel
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverResponseModel
import com.techlogix.pacabs_driver.models.serviceTypeModels.ServiceType
import com.techlogix.pacabs_driver.models.vehicleModels.VehiclesResponseModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacaps.utility.Utility
import com.techlogix.pacaps.utility.Utility.Companion.getCurrentDateTime
import com.work.mynotepad.adapters.GenericSpinnerAdapter
import kotlinx.android.synthetic.main.fragment_signup.*



class SignupFragment<T> : Fragment(), APIManager.CallbackGenric<T> {
    var baseActivity: BaseActivity? = null
    var citiSpinnerAdapter: GenericSpinnerAdapter? = null
    var vehicleTypeSpinnerAdapter: GenericSpinnerAdapter? = null
    var serviceTypeSpinnerAdapter: GenericSpinnerAdapter? = null
    var selectedCity: Long? = null
    var selectedVehicle: Long? = null
    var selectedWork: Long? = 1
    var selectedService: Long? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)


//        val text =
//            fromHtml(" I've read and agreed the <b><u> <font size=\"6\"\n" + "          face=\"verdana\"\n" + "          color=\"#051CC4\">Terms and Conditions</font></u><b>")
//        termConsCheckbox.setText(text)
//        baseActivity = activity as BaseActivity
//        getOtpBtn.setOnClickListener {
//            if (validateFeilds()) {
//                APIManager.getInstance()
//                    .createUser(CreateUserRequestModel(fullNameEd.text.toString(),
//                        numberEd.text.toString(),
//                        emailEd.text.toString(),
//                        passwordEd.text.toString(),
//                        "12323",
//                        ""), this)
//            }
//
//
//        }
    }

    private fun init(view: View) {

        baseActivity = activity as BaseActivity
//library use
        LocationHandler(context).setLocationListener(LocationListener {
            // Get the best known location
            Utility.currentUserLatLng = LatLng(it.latitude, it.longitude)
        }).start()

        APIManager.getInstance().getAllCities(this)
        APIManager.getInstance().getAllVehicleTypes(this)
        APIManager.getInstance().getAllServiceTypes(this)

        citiSpinnerAdapter = GenericSpinnerAdapter(
            requireContext(),
            R.id.spinner_item_tv,
            R.id.spinner_item_tv
        )

        vehicleTypeSpinnerAdapter = GenericSpinnerAdapter(
            requireContext(),
            R.id.spinner_item_tv,
            R.id.spinner_item_tv
        )
        serviceTypeSpinnerAdapter = GenericSpinnerAdapter(
            requireContext(),
            R.id.spinner_item_tv,
            R.id.spinner_item_tv
        )

        var s: String = getCurrentDateTime();

        signupBtn.setOnClickListener {
//            findNavController().navigate(LoginSinUpFragmentDirections.gotoOTPFragmentAction())
            if (validateFeilds()) {/*
                APIManager.getInstance()
                    .createDriver(
                        CreateDriverRequestModel(
                            1,
                            fisrtNameEd.text.toString(),
                            passwordEd.text.toString(),
                            mobileNumberEd.text.toString(),
                            vehicleNumberEd.text.toString(),
                            this!!.selectedVehicle!!,
                            this!!.selectedCity!!,
                            1,
                            Utility.currentUserLatLng!!.latitude,
                            Utility.currentUserLatLng!!.latitude,
                            getCurrentDateTime(),
                            "",
                            this!!.selectedWork!!,
                            this!!.selectedService!!,
                            "",
                            accountNumberEd.text.toString(),
                            bankNameEd.text.toString(),
                            accountNameasBankEd.text.toString(),
                            ifscCodeEd.text.toString()
                        ), this
                    )*/
            }

        }

        selectedSpinners()
    }

    private fun validateFeilds(): Boolean {
        if (fisrtNameEd.text.toString().isEmpty()) {
            fisrtNameEd.setError("Please enter fisrt name")
            fisrtNameEd.requestFocus()
            return false
        } else if (secondNameEd.text.toString().isEmpty()) {
            secondNameEd.setError("Please enter second name")
            secondNameEd.requestFocus()
            return false
        } else if (mobileNumberEd.text.toString().isEmpty()) {
            mobileNumberEd.setError("Please enter mobile number")
            mobileNumberEd.requestFocus()
            return false
        } else if (vehicleNumberEd.text.toString().isEmpty()) {
            vehicleNumberEd.setError("Please enter vehicle number")
            vehicleNumberEd.requestFocus()
            return false
        } else if (passwordEd.text.toString().isEmpty()) {
            passwordEd.setError("Please enter password")
            passwordEd.requestFocus()
            return false
        } else if (confirmPasswordEd.text.toString().isEmpty()) {
            confirmPasswordEd.setError("Please enter confirm password")
            confirmPasswordEd.requestFocus()
            return false
        } else if (accountNumberEd.text.toString().isEmpty()) {
            accountNumberEd.setError("Please enter account number")
            accountNumberEd.requestFocus()
            return false
        } else if (bankNameEd.text.toString().isEmpty()) {
            bankNameEd.setError("Please enter bank name")
            bankNameEd.requestFocus()
            return false
        } else if (accountNameasBankEd.text.toString().isEmpty()) {
            accountNameasBankEd.setError("Enter name as per bank")
            accountNameasBankEd.requestFocus()
            return false
        } else if (ifscCodeEd.text.toString().isEmpty()) {
            ifscCodeEd.setError("Please enter IFSC code")
            ifscCodeEd.requestFocus()
            return false
        }


        fisrtNameEd.setError(null)
        secondNameEd.setError(null)
        mobileNumberEd.setError(null)
        vehicleNumberEd.setError(null)
        passwordEd.setError(null)
        confirmPasswordEd.setError(null)
        accountNumberEd.setError(null)
        bankNameEd.setError(null)
        accountNameasBankEd.setError(null)
        ifscCodeEd.setError(null)

        return true
    }


    private fun fromHtml(source: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(source)
        }
    }


    fun selectedSpinners() {
        citySpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedCity =
                        (parent?.getItemAtPosition(position) as CititesResponseModel).int.toLong()
                }
            }
        vehicleSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedVehicle =
                        (parent?.getItemAtPosition(position) as VehiclesResponseModel).idval.toLong()
                }
            }


        workTypeSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
//                    selectedVehicle=(parent?.getItemAtPosition(position) as Type).idval.toLong()
                }
            }

        serviceTypeSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedService =
                        (parent?.getItemAtPosition(position) as ServiceType).id.toLong()
                }
            }
    }

    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result != null && requestCode == Utility.LOV_CITIES) {
            citiSpinnerAdapter!!.setData(response.result as ArrayList<CititesResponseModel>)
            citySpinner.adapter = citiSpinnerAdapter
        } else if (response!!.result != null && requestCode == Utility.LOV_VEHICLES) {
            vehicleTypeSpinnerAdapter!!.setData(response.result as ArrayList<VehiclesResponseModel>)
            vehicleSpinner.adapter = vehicleTypeSpinnerAdapter
        } else if (response!!.result != null && requestCode == Utility.LOV_SERVICE_TYPE) {
            serviceTypeSpinnerAdapter!!.setData(response.result as ArrayList<ServiceType>)
            serviceTypeSpinner.adapter = serviceTypeSpinnerAdapter
        }
        if (response!!.result is CreateDriverResponseModel)
        {
            PacabDriver.loginResponse=response.result as CreateDriverResponseModel
            findNavController().navigate(LoginSinUpFragmentDirections.gotoOTPFragmentAction())
        }
    }


}