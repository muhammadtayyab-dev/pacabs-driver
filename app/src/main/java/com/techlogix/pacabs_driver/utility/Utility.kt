package com.techlogix.pacaps.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.techlogix.pacabs_driver.activities.BaseActivity
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Utility {

    companion object {
        val LOCATION_PERMISSIONS_CODE=121

        val PROVIDER_CAB_TIME_IN_KM_RECYCLER_TYPE: Int = 1
        val PROVIDER_CAB_CAR_TYPE_RECYCLER_TYPE: Int = 2
        val NAV_ITEMS: Int = 3
        val MY_RIDES: Int = 4
        val OFFERS_DISCOUNT: Int = 5
        val MY_FAVORITES: Int = 6
        val MY_BOOKING: Int = 7
        val MY_WALLET: Int = 8
        val EARNING_FILTERS: Int = 9
        val TRIP_STATICS: Int = 10
        val MY_JOBS: Int = 11
        val DRIVER_TYPE: Int = 12
        val MANAGE_CABS: Int = 13


        val LOV_CITIES=101
        val LOV_VEHICLES=102
        val LOV_WORK_TYPE=103
        val LOV_SERVICE_TYPE=104

        val UPDATE_AVAILABILITY=110
        val JOB_REQUEST=111
        val JOB_ACKNWOLEDGE=112
        val ACCEPT_JOB_REQUEST=113
        val REJECT_JOB_REQUEST=114
        val END_JOB_REQUEST=115
        val POLLING=116
        val CURRENT_JOB_REQUEST=117
        val UPDATE_TOKEN=118


        var currentUserLatLng: LatLng? = LatLng(0.0,0.0);
        var previousLocation: Location? = null;
        var currentLocation: Location? = null;


        fun bitmapDescriptorFromVector(context: Context,
                                       @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {
            val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
            val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
            vectorDrawable!!.setBounds(40,
                20,
                vectorDrawable.intrinsicWidth + 40,
                vectorDrawable.intrinsicHeight + 20)
            val bitmap = Bitmap.createBitmap(background.intrinsicWidth,
                background.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            background.draw(canvas)
//            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        fun emailValidator(email: String?): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun getCurrentDateTime(): String {
            val df: DateFormat = SimpleDateFormat("yyyy-MM-d HH:mm:ss")
            val date: String = df.format(Calendar.getInstance().getTime())
            return date
        }

        fun getSpeed(): Double {

            var newLocation: Location
            var oldLocation: Location

            if (previousLocation == null && currentLocation == null)
                return 0.0
            else {
                oldLocation =  previousLocation!!
                newLocation = currentLocation!!

                val newLat = newLocation.latitude
                val newLon = newLocation.longitude
                val oldLat = oldLocation.latitude
                val oldLon = oldLocation.longitude

                return if (newLocation.hasSpeed())
                {
//                Toast.makeText(this, currentLocation.speed.toDouble().toString(),Toast.LENGTH_SHORT).show()
                    newLocation.speed.toDouble()


                }
                else return 0.0

                /*else {
                    val radius = 6371000.0
                    val dLat = Math.toRadians(newLat - oldLat)
                    val dLon = Math.toRadians(newLon - oldLon)
                    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(Math.toRadians(newLat)) * Math.cos(
                        Math.toRadians(
                            oldLat
                        )
                    ) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2)
                    val c = 2 * Math.asin(Math.sqrt(a))
                    val distance = Math.round(radius * c).toDouble()
                    val timeDifferent = newLocation.time - oldLocation.time.toDouble()
                     return distance / timeDifferent
                }*/
            }
        }

        fun getAccuracy(): Long {
            if (currentLocation != null && currentLocation!!.hasAccuracy()) {
                return currentLocation!!.accuracy.toLong()
            }
            return 0L
        }
        fun loadJsonDataFromAsset(activity: BaseActivity):String{
            var json: String? = null
            json = try {
                val inputStream: InputStream = activity.getAssets().open("routefile.json")
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
                return ""
            }
            return json
        }
    }




}