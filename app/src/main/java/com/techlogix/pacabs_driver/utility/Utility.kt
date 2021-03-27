package com.techlogix.pacaps.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.util.regex.Pattern


class Utility {

    companion object {
        val HEADER_TYPE=0
        val SERVICE_PROVIDER_TYPE: Int = 11
        val DRIVER_TYPE: Int = 12
        val PROVIDER_CAB_TIME_IN_KM_RECYCLER_TYPE: Int = 1
        val PROVIDER_CAB_CAR_TYPE_RECYCLER_TYPE: Int = 2
        val NAV_ITEMS: Int = 3
        val MY_RIDES: Int = 4
        val OFFERS_DISCOUNT: Int = 5
        val MY_FAVORITES: Int = 6
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
    }
}