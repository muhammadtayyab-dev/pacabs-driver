package com.techlogix.pacabs_driver.fragments.RidesAndGetRidesFragmenmts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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
import com.google.android.gms.maps.model.*
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.BaseActivity
import com.techlogix.pacabs_driver.activities.RidesAndGetRidesActivty
import com.techlogix.pacabs_driver.dialogs.EnterOTPDialog
import com.techlogix.pacabs_driver.dialogs.OTPErrorDialog
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacabs_driver.models.directionParserModels.DataParser
import com.techlogix.pacabs_driver.models.eventsModel.UpdateEventRequestModel
import com.techlogix.pacabs_driver.models.jobModels.CompletedRideModel
import com.techlogix.pacabs_driver.models.jobModels.EndRideResponseModel
import com.techlogix.pacabs_driver.models.jobModels.JobRequestModel
import com.techlogix.pacabs_driver.network.APIManager
import com.techlogix.pacabs_driver.utility.MapAnimator
import com.techlogix.pacabs_driver.utility.PermissionUtils
import com.techlogix.pacabs_driver.utility.SharePrefData
import com.techlogix.pacaps.dialogs.AlertDialogCallback
import com.techlogix.pacaps.utility.Utility
import com.tecorb.hrmovecarmarkeranimation.AnimationClass.HRMarkerAnimation
import com.tecorb.hrmovecarmarkeranimation.CallBacks.UpdateLocationCallBack
import kotlinx.android.synthetic.main.pickup_information_bottomsheet_layout.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class PickupRideFragment<T> : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
    GoogleMap.SnapshotReadyCallback, LocationListener,   /*RoutingListener,*/
    APIManager.CallbackGenric<T> {

    var TAG = "Location"
    var TAGStart =""
    var TAGEnd = ""
    var baseActivity: BaseActivity? = null
    var startotp: String? = ""

    var destinationLocation: String? = null
    var destinationLat: Double? = null
    var destinationLong: Double? = null
    var userContactNumber: String? = null
    var markerCount: Int = 0;

    var googleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    var googleAPIClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var mCurrentMarker: Marker? = null

    //    var mStartMaker: Marker? = null
    var mDestinationMarker: Marker? = null
    var mLastLocation: Location? = null
    var oldLocation: Location? = null
    private lateinit var routeHandler: Handler
    private lateinit var routeRunnable: Runnable
    var parser: DataParser? = null
    var parserTask: ParserTask? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pickup_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    fun initViews(view: View) {

        baseActivity = requireActivity() as BaseActivity
        requireActivity().setTitle("Pickup")

        /* if (PacabDriver.rideStatus == 0) {
             changeBtnText(getString(R.string.start_ride))
         } else if (PacabDriver.rideStatus == 1) {
             changeBtnText(getString(R.string.end_ride))
         }*/
        TAGStart = getString(R.string.start_ride)
        TAGEnd =  getString(R.string.end_ride)


        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            mapFragment =
                childFragmentManager.findFragmentById(R.id.usersMaps) as SupportMapFragment;
            mapFragment?.getMapAsync(this)

            startRideBtn.setOnClickListener(this)
            callImg.setOnClickListener(this)
            chatImg.setOnClickListener(this)
            navigationImg.setOnClickListener(this)


            if ((activity as RidesAndGetRidesActivty<T>).acceptedResponse != null) {
                var response = (activity as RidesAndGetRidesActivty<T>).acceptedResponse
                pickupatLocNameTv.text = response?.pickuplocation ?: ""
                customerNameTv.text = response?.username ?: ""

                destinationLocation = response?.destinationLocation
                destinationLat = response?.destinationlatitude
                destinationLong = response?.destinationlongitude
                startotp = response?.startotp ?: ""
                userContactNumber = response?.mobile ?: ""

                if (response?.bookingStatus == StatusIDEnums.Assigned.statusID.value.toInt()) {
                    startRideBtn.setText(getString(R.string.start_ride))
                } else if (response?.bookingStatus == StatusIDEnums.Started_Ride.statusID.value.toInt()) {
                    startRideBtn.setText(getString(R.string.end_ride))
                }

            } else {
                PermissionUtils.requestLocationPermissions(
                    activity,
                    Utility.LOCATION_PERMISSIONS_CODE
                )
            }

        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
//        googleMap?.isMyLocationEnabled = true
        val locationButton =
            (mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<ImageView>(
                Integer.parseInt("2")
            )

        locationButton?.setImageResource(R.drawable.current_loc)

        val layoutParams: RelativeLayout.LayoutParams =
            locationButton?.getLayoutParams() as RelativeLayout.LayoutParams
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)

        this.googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap?.getUiSettings()?.setZoomControlsEnabled(false);
        this.googleMap?.getUiSettings()?.setZoomGesturesEnabled(true);
        this.googleMap?.getUiSettings()?.setCompassEnabled(true);


        //Initialize Google Play Services
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            buildGoogleApiClient();
//            this.googleMap?.setMyLocationEnabled(true);
        } else {
            buildGoogleApiClient();
//            this.googleMap?.setMyLocationEnabled(true);
        }

        if (destinationLat != null && destinationLong != null && !destinationLat!!.equals("0.0") && !destinationLong!!.equals(
                "0.0"
            )
        ) {
            showPath()
        }

    }

    private fun buildGoogleApiClient() {
        googleAPIClient = GoogleApiClient.Builder(context).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        googleAPIClient?.connect()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG, "onConnected - isConnected ...............: " + googleAPIClient!!.isConnected());
        startLocationUpdates();
    }

    fun startLocationUpdates() {
        mLocationRequest = LocationRequest();
        mLocationRequest?.interval = 1000
        mLocationRequest?.fastestInterval = 1000
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
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


    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location) {
        try {

            Log.d("NewLatLng", ("Lati: " + p0.latitude + ", Longi:" + p0.longitude))
/*
            googleMap!!.clear()
*/

            if (p0 != null) {
                Utility.currentUserLatLng = (LatLng(p0.latitude, p0.longitude))
                mLastLocation = p0

/*
                val distance: Double = SphericalUtil.computeDistanceBetween(
                    Utility.currentUserLatLng,
                    LatLng(destinationLat!!, destinationLong!!)
                )
                updateDistance_Time("", distance.toString())
*/

            }

            /* mCurrentMarker = googleMap?.addMarker(
                 MarkerOptions().position(
                     LatLng(
                         Utility.currentUserLatLng!!.latitude,
                         Utility.currentUserLatLng!!.longitude
                     )
                 ).icon(
                     Utility.bitmapDescriptorFromVector(
                         requireActivity(),
                         R.drawable.provider_marker
                     )
                 )
             )*/

/*
             mDestinationMarker = googleMap?.addMarker(
                MarkerOptions().position(LatLng(destinationLat!!, destinationLong!!)).icon(
                    Utility.bitmapDescriptorFromVector(
                        requireActivity(),
                        R.drawable.user_marker
                    )
                )
            )
*/

            if (markerCount === 1) {
                if (oldLocation != null) {
                    HRMarkerAnimation(googleMap, 1000,
                        UpdateLocationCallBack { updatedLocation ->
                            oldLocation = updatedLocation
                        }).animateMarker(mLastLocation, oldLocation, mCurrentMarker)

                } else {
                    oldLocation = mLastLocation
                }
            } else if (markerCount === 0) {
                if (mCurrentMarker != null) {
                    mCurrentMarker!!.remove()
                }

                val latLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
                mCurrentMarker = googleMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            mLastLocation!!.latitude,
                            mLastLocation!!.longitude
                        )
                    )
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon))
                )
                googleMap!!.setPadding(2000, 4000, 2000, 4000)
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))

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


    fun showPath() {
        routeHandler = Handler()
        routeRunnable = Runnable {
            run {
                cancelRide() // first check if statusId == cancel so cancel the ride, (this function is calling here to avoide extra handler)
                if (context != null) {

                    val url = getUrl(
                        Utility.currentUserLatLng!!.latitude,
                        Utility.currentUserLatLng!!.longitude,
                        destinationLat!!,
                        destinationLong!!
                    )
                    val fetchUrl = FetchUrl()
                    fetchUrl.execute(url)
                }
            }
        }
        routeRunnable.run()
    }


    fun cancelRide() {
        /* Cancel Ride flow*/
        if (PacabDriver.statusID == StatusIDEnums.Cancelled.statusID.value) {

            baseActivity!!.resetAllIDs()
            requireActivity().finish()
        }
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.startRideBtn) {
            if (startRideBtn.text.equals(TAGStart)) {
                EnterOTPDialog(requireContext(), startotp.toString(), object : AlertDialogCallback {
                    override fun onDissmiss(flag: Boolean) {
                        if (!flag) {
                            OTPErrorDialog(requireContext(), object : AlertDialogCallback {
                                override fun onDissmiss(flag: Boolean) {
                                    //otp not matched,
                                }
                            }).show()
                        } else {
//                            PacabDriver.rideStatus = 1
//                            SharePrefData.getInstance().setLOCAL_RIDE_STATUS(1)
                            updateEventStatusCall(StatusIDEnums.Started_Ride.statusID.value)
                            startRideBtn.text = getString(R.string.end_ride)
                        }
                    }
                }).show()
            } else if (startRideBtn.text.equals(TAGEnd)) {
                APIManager.getInstance().jobEnd(
                    JobRequestModel(
                        PacabDriver.flowID,
                        PacabDriver.bookingID,
                        PacabDriver.loginResponse!!.vehicleid,
                        Utility.currentUserLatLng!!.latitude,
                        Utility.currentUserLatLng!!.longitude,
                        StatusIDEnums.Completed_Ride.statusID.value
                    ), this
                )
            }
        } else if (view?.id == R.id.callImg) {
            if (isPermissionGranted()) {
                call_action();
            }
        } else if (view?.id == R.id.chatImg) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.fromParts("sms", userContactNumber, null)
                )
            )
        } else if (view?.id == R.id.navigationImg) {
            if (destinationLat != null && destinationLong != null) {
                val naviUri2 = Uri.parse(
                    "http://maps.google.com/maps?"
                            + "saddr=" + Utility.currentUserLatLng!!.latitude + "," + Utility.currentUserLatLng!!.longitude
                            + "&daddr=" + destinationLat + "," + destinationLong
                )
                val intent = Intent(Intent.ACTION_VIEW, naviUri2)
                intent.setClassName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"
                )
                startActivity(intent)
            } else
                Toast.makeText(requireContext(), "Unable to draw Route", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSnapshotReady(googleMapSnap: Bitmap?) {

    }


    private fun updateDistance_Time(durationText: String?, distanceText: String?) {
        try {
            if (timeTv != null)
                timeTv.text = durationText
            if (distanceTv != null)
                distanceTv.text = distanceText
        } catch (e: java.lang.Exception) {
            Log.d("error", e.message.toString())
        }
    }


    override fun onResult(response: GenericResponseModel<T>?, requestCode: Int) {
        if (response!!.result is EndRideResponseModel) {

            parserTask!!.cancel(true)
            stopAnim()
            Handler().removeCallbacks(routeRunnable)
            routeHandler.removeCallbacksAndMessages(null);


            //goto RideCompleted Fragment
            var res = response.result as EndRideResponseModel

            var completedRideModel = CompletedRideModel(
                res.estimatedCost,
                res.maxCost,
                res.dist,
                res.fare,
                destinationLocation!!,
                pickupatLocNameTv.text.toString()
            )
            val action: NavDirections =
                PickupRideFragmentDirections.gotoEndRideFragment(completedRideModel)
            findNavController().navigate(action)

        }
    }


    /*call customer persmission and logic*/

    fun call_action() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.setData(Uri.parse("tel:$userContactNumber"))
        startActivity(callIntent)
    }

    fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("TAG", "Permission is granted")
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                requestPermissions(
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                    call_action()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }


    fun startAnim(routeList: ArrayList<LatLng?>?) {
        if (googleMap != null && routeList!!.size > 1) {
            MapAnimator.getInstance().animateRoute(googleMap, routeList)
        } else {
            Toast.makeText(context, "Map not ready", Toast.LENGTH_LONG).show()
        }
    }

    fun stopAnim() {
        if (googleMap != null) {
            MapAnimator.getInstance().stopAnim()
        } else {
            Toast.makeText(context, "Map not ready", Toast.LENGTH_LONG).show()
        }
    }


    // Fetches data from url passed
    inner class FetchUrl : AsyncTask<String?, Void?, String>() {
        override fun doInBackground(vararg url: String?): String {
            // For storing data from web service
            var data = ""
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]!!).toString()
                Log.d("Background Task data", data)
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data

        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask!!.execute(result)
        }
    }


    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection!!.inputStream
            val br =
                BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            Log.d("downloadUrl", data)
            br.close()
        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }


    @Suppress("DEPRECATION")
    inner class ParserTask : AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {

            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? =
                null
            try {
                jObject = JSONObject(jsonData[0])
                Log.d("ParserTask", jsonData[0].toString())
                parser = DataParser()
                Log.d("ParserTask", parser.toString())


                // Starts parsing data
                routes = parser!!.parse(jObject)
                Log.d("ParserTask", "Executing routes")
                Log.d("ParserTask", routes.toString())
            } catch (e: java.lang.Exception) {
                Log.d("ParserTask", e.toString())
                e.printStackTrace()
            }
            return routes
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var points: ArrayList<LatLng?>? = null
            var lineOptions: PolylineOptions? = null

            updateDistance_Time(parser!!.duration, parser!!.distance)

            // Traversing through all the routes
            for (i in result!!.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path =
                    result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]
                    val lat =
                        Objects.requireNonNull(point["lat"])!!.toDouble()
                    val lng =
                        Objects.requireNonNull(point["lng"])!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                /*if (!Utility.currentUserLatLng!!.latitude.toString().equals(
                        "",
                        ignoreCase = true
                    ) && !Utility.currentUserLatLng!!.longitude.toString()
                        .equals("", ignoreCase = true)
                ) {
                    val location = LatLng(
                        Utility.currentUserLatLng!!.latitude,
                        Utility.currentUserLatLng!!.longitude
                    )
                    //mMap.clear();
                    if (mStartMaker != null)
                        mStartMaker!!.remove()
                    val markerOptions = MarkerOptions()
                        .anchor(0.5f, 0.75f)
                        .position(location).title("Source").draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker))
                    mCurrentMarker = googleMap!!.addMarker(markerOptions)
                    mStartMaker = googleMap!!.addMarker(markerOptions)
                    //CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(18).build();
                    //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }*/
                if (!destinationLat.toString()!!
                        .equals("", ignoreCase = true) && !destinationLong!!.toString()
                        .equals("", ignoreCase = true)
                ) {
                    var destLatLng =
                        LatLng(destinationLat!!.toDouble(), destinationLong!!.toDouble())
                    if (mDestinationMarker != null)
                        mDestinationMarker!!.remove()
                    val destMarker = MarkerOptions()
                        .position(destLatLng).title("Destination").draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker))
                    mDestinationMarker = googleMap!!.addMarker(destMarker)

//                    val builder = LatLngBounds.Builder()
//                    builder.include(mStartMaker!!.getPosition())
//                    builder.include(mDestinationMarker!!.getPosition())
//                    val bounds = builder.build()
//                    val padding = 150 // offset from edges of the map in pixels
//                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(25f)
                lineOptions.color(Color.BLUE)
                Log.d("onPostExecute", "onPostExecute lineoptions decoded")
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                //mMap.addPolyline(lineOptions);
                startAnim(points)
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }
            routeHandler.postDelayed(routeRunnable, 8000)
        }


    }


    private fun getUrl(
        source_latitude: Double,
        source_longitude: Double,
        dest_latitude: Double,
        dest_longitude: Double
    ): String? {

        // Origin of route
        val str_origin = "origin=$source_latitude,$source_longitude"

        // Destination of route
        val str_dest = "destination=$dest_latitude,$dest_longitude"

        // Sensor enabled
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.map_key)

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$key"


        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        if (googleAPIClient != null && googleAPIClient!!.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleAPIClient, this)
            stopAnim()
        }
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

    fun updateEventStatusCall(statusId: Long) {
        APIManager.getInstance().updateStatus(
            UpdateEventRequestModel(
                PacabDriver.bookingID,
                PacabDriver.loginResponse!!.vehicleid,
                Utility.currentUserLatLng!!.latitude,
                Utility.currentUserLatLng!!.longitude,
                statusId
            ), this
        )
    }

}
