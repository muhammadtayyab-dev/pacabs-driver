package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.littlemango.stacklayoutmanager.StackLayoutManager
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.activities.DashboardActivity
import com.techlogix.pacabs_driver.activities.RidesAndGetRidesActivty
import com.techlogix.pacabs_driver.adapters.ActivityGenericAdapte
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.jobModels.JobRejectResponseModel
import com.techlogix.pacabs_driver.models.jobModels.JobRequestModel
import com.techlogix.pacabs_driver.models.jobModels.JobResponseModel
import com.techlogix.pacabs_driver.models.jobModels.MyJobsModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacabs_driver.utility.PermissionUtils
import com.techlogix.pacaps.utility.GenericCallback
import com.techlogix.pacaps.utility.Utility
import com.tecorb.hrmovecarmarkeranimation.AnimationClass.HRMarkerAnimation
import com.tecorb.hrmovecarmarkeranimation.CallBacks.UpdateLocationCallBack
import java.lang.Exception
import java.util.ArrayList

class DashboardMainFragment<T> : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, GenericCallback<T>,
    APIManager.CallbackGenric<T> {
    var markerCount: Int = 0
    var googleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    var googleAPIClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var mCurrentMarker: Marker? = null
    var jobsRecycler: RecyclerView? = null
    var jobsAdapter: ActivityGenericAdapte<T>? = null
    var baseActivity: BaseActivity? = null
    val list = arrayListOf<JobResponseModel>()
    var marker: Marker? = null
    var TAG = "Location"
    var mLastLocation: Location? = null
    var oldLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun setData() {
        baseActivity = requireActivity() as BaseActivity

        val stackLayoutManager =
            StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        stackLayoutManager.setPagerMode(true)
        stackLayoutManager.setPagerFlingVelocity(3000)
        jobsRecycler?.layoutManager = stackLayoutManager

    }


    private fun initViews(view: View) {
        jobsRecycler = view.findViewById(R.id.jobsRecycler)


        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            mapFragment =
                childFragmentManager.findFragmentById(R.id.usersMaps) as SupportMapFragment;
            mapFragment?.getMapAsync(this)

            setData()


        } else {
            PermissionUtils.requestLocationPermissions(activity, Utility.LOCATION_PERMISSIONS_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.isMyLocationEnabled = true

        val locationButton =
            (mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<ImageView>(
                Integer.parseInt("2")
            )

        /* locationButton?.background=ContextCompat.getDrawable(this,R.drawable.back_arrow)
         locationButton?.setBackgroundColor(ContextCompat.getColor(this,R.color.zong_pink))*/
        locationButton?.setImageResource(R.drawable.current_loc)

        val layoutParams: RelativeLayout.LayoutParams =
            locationButton?.getLayoutParams() as RelativeLayout.LayoutParams
        // position on right bottom
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)

        this.googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap?.getUiSettings()?.setZoomControlsEnabled(false);
        this.googleMap?.getUiSettings()?.setZoomGesturesEnabled(true);
        this.googleMap?.getUiSettings()?.setCompassEnabled(true);

        //Initialize Google Play Services
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            buildGoogleApiClient();
            this.googleMap?.setMyLocationEnabled(true);
        } else {
            buildGoogleApiClient();
            this.googleMap?.setMyLocationEnabled(true);
        }
    }


    protected fun buildGoogleApiClient() {
        googleAPIClient = GoogleApiClient.Builder(context).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        googleAPIClient?.connect()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Utility.LOCATION_PERMISSIONS_CODE) {
//            mapFragment =
//                childFragmentManager.findFragmentById(R.id.usersMaps) as SupportMapFragment;
//            mapFragment?.getMapAsync(this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG, "onConnected - isConnected ...............: " + googleAPIClient!!.isConnected());
        startLocationUpdates();
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    fun startLocationUpdates() {
        mLocationRequest = LocationRequest();
        mLocationRequest?.interval = 1000
        mLocationRequest?.fastestInterval = 1000
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleAPIClient,
                mLocationRequest,
                this
            )
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)
    }

    protected fun stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleAPIClient, this)
            Log.d(TAG, "Location update stopped .......................")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(p0: Location) {
        try {

            Utility.currentUserLatLng = (LatLng(p0.latitude, p0.longitude))
            mLastLocation = p0

            if (markerCount === 1) {
                if (oldLocation != null) {
                    HRMarkerAnimation(googleMap, 1000,
                        UpdateLocationCallBack { updatedLocation ->
                            oldLocation = updatedLocation
                        }).animateMarker(mLastLocation, oldLocation, marker)
                } else {
                    oldLocation = mLastLocation
                }
            } else if (markerCount === 0) {
                if (marker != null) {
                    marker!!.remove()
                }

                val latLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
                marker = googleMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            mLastLocation!!.latitude,
                            mLastLocation!!.longitude
                        )
                    )
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon))
                )
                googleMap!!.setPadding(2000, 4000, 2000, 4000)
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                /*################### Set Marker Count to 1 after first marker is created ###################*/
                markerCount = 1

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }


        } catch (e: Exception) {
            Log.e("exx", e.message + "")
        }
    }

    override fun GenericCallType(T: Any) {
        if (T is JobResponseModel) // means accept button is clicked, so call Accept ride api AND ALSO STOP THE JOB REQUEST HANDLER
        { //stop the JobRequest Handler

            (activity as DashboardActivity<T>).isJobRunning = false
            Handler().removeCallbacks((activity as DashboardActivity<T>).runnableJob)

            // update the Booking ID here for whole app scenario
            APIManager.getInstance().jobAcceptance(
                JobRequestModel(
                    PacabDriver.flowID,
                    PacabDriver.bookingID,
                    PacabDriver.loginResponse!!.vehicleid,
                    Utility.currentUserLatLng!!.latitude,
                    Utility.currentUserLatLng!!.longitude
                ), this
            )

        } else if (T is Int) // it means cancel ride is clicked, so call Reject ride api
        {
            APIManager.getInstance().jobRejectance(
                JobRequestModel(
                    PacabDriver.flowID,
                    PacabDriver.bookingID,
                    PacabDriver.loginResponse!!.vehicleid,
                    Utility.currentUserLatLng!!.latitude,
                    Utility.currentUserLatLng!!.longitude
                ), this
            )
        }

    }


    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (requestCode == Utility.JOB_REQUEST) {
            Log.i("response", response!!.message)
            if (list.isNullOrEmpty() && response.result is JobResponseModel) {
                list.add(response.result as JobResponseModel)
                setRecyclerData()
                //empty the response, so next time you did't get the same response here
                response.result = null
            }
        } else if (requestCode == Utility.JOB_ACKNWOLEDGE && response!!.result is JobResponseModel) {
            baseActivity!!.updateBookingID((response!!.result as JobResponseModel).bookingid.toLong())
            baseActivity!!.updateFlowID((response!!.result as JobResponseModel).id.toLong())
        } else if (requestCode == Utility.ACCEPT_JOB_REQUEST) {

            baseActivity!!.updateBookingID((response!!.result as JobResponseModel).bookingid.toLong())
            baseActivity!!.updateFlowID((response!!.result as JobResponseModel).id.toLong())

            // save response offline in SharedPref
//            SharePrefData.getInstance().setACCEPTED_JOB_RESPONSE(response!!.result as JobResponseModel)

            val myIntent = Intent(activity, RidesAndGetRidesActivty::class.java)
            myIntent.putExtra("acceptedResponse", response!!.result as JobResponseModel)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(myIntent)

        } else if (requestCode == Utility.REJECT_JOB_REQUEST) {
            //clear list of recyvler and hide the recycler of JOBS when response is true
            if (response!!.result != null && (response.result as JobRejectResponseModel).status == 1.0) {
                baseActivity!!.updateBookingID(0)
                baseActivity!!.updateStatusID(0)
                baseActivity!!.updateFlowID(0)
                list.clear()

                (activity as DashboardActivity<T>).isJobRunning = true
                (activity as DashboardActivity<T>).runnableJob.run()

                jobsRecycler!!.visibility = View.GONE
            }
        }
    }

    fun clearData() {
        if (jobsAdapter != null) {
            list.clear()
            jobsAdapter!!.notifyDataSetChanged()
            jobsRecycler!!.visibility = View.GONE
        }

    }

    private fun setRecyclerData() {
        jobsRecycler!!.visibility = View.VISIBLE
        jobsAdapter =
            ActivityGenericAdapte(this!!.context!!, Utility.MY_JOBS, list as ArrayList<*>, this)
        jobsRecycler?.adapter = jobsAdapter
        jobsAdapter!!.notifyDataSetChanged()
        callJobAcknowledgmentApi()
    }

    private fun callJobAcknowledgmentApi() {
        APIManager.getInstance().jobAcknowledegment(
            JobRequestModel(
                PacabDriver.flowID,
                PacabDriver.bookingID,
                PacabDriver.loginResponse!!.vehicleid,
                Utility.currentUserLatLng!!.latitude,
                Utility.currentUserLatLng!!.longitude
            ), this
        )
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        markerCount = 0
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        if (googleAPIClient != null && googleAPIClient!!.isConnected()) {
            startLocationUpdates();
        }
    }

    override fun onStart() {
        super.onStart()
        if (googleAPIClient != null && !googleAPIClient!!.isConnected()) {
            googleAPIClient!!.connect()
        }
    }
}