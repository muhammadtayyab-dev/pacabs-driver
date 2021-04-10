package com.techlogix.pacabs_driver.fragments.splashIntroScreensFrags

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.activities.RegistrationLoginActivity
import com.techlogix.pacabs_driver.utility.PermissionUtils
import com.techlogix.pacaps.utility.Utility
import kotlinx.android.synthetic.main.fragment_enable_location.*

class EnableLocationFragment : Fragment() {
    var baseActivity: BaseActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enable_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        baseActivity = requireActivity() as BaseActivity
        useMyLocBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionUtils.requestLocationPermissions(
                    requireActivity(),
                    Utility.LOCATION_PERMISSIONS_CODE
                )
            } else {
                baseActivity?.openActivity(RegistrationLoginActivity::class.java, null)
            }
        }
        skipForNow.setOnClickListener {
            baseActivity?.openActivity(RegistrationLoginActivity::class.java, null)
            baseActivity?.finish()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utility.LOCATION_PERMISSIONS_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                baseActivity?.openActivity(RegistrationLoginActivity::class.java, null)
                baseActivity?.finish()
            } else {
                baseActivity?.showToast("Permission not granted")
            }
        }
    }

}