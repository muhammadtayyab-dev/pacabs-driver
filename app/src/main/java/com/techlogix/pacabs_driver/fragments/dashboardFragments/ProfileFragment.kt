package com.techlogix.pacabs_driver.fragments.dashboardFragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.activities.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_offline.*

class ProfileFragment<T> : Fragment() , View.OnClickListener {
    var baseActivity: BaseActivity? = null

    val NOTIFICATION_ID = 101
    val NOTIFICATION_CHANNEL_ID = "channel_id"
    val CHANNEL_NAME = "HireCabs"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)
    }

    private fun init(view: View) {
        baseActivity = activity as BaseActivity

        bookingImg.setOnClickListener(this)
        myBookingLayout.setOnClickListener(this)
        tripStatisticsLayout.setOnClickListener(this)
        walletImg.setOnClickListener(this)
        topupImg.setOnClickListener(this)
        notificationLayout.setOnClickListener(this)
        userFeedBackLayout.setOnClickListener(this)


        userNameTv.text = PacabDriver.loginResponse!!.name
        emailTv.text = PacabDriver.loginResponse!!.mobile // show here email next time
     }
    override fun onResume() {
        super.onResume()
        walletBalanceValueTv.text =
            getString(R.string.indian_currency) + " " + PacabDriver.loginResponse!!.walletbalance.toString()
    }
    override fun onClick(v: View?) {
        if (v!!.id.equals(R.id.bookingImg)) {
            (activity as DashboardActivity<T>).disableSwipeViewPager.setCurrentItem(1, true)
        } else if (v!!.id.equals(R.id.myBookingLayout)) {
            (activity as DashboardActivity<T>).disableSwipeViewPager.setCurrentItem(1, true)
        }
        else if (v!!.id.equals(R.id.tripStatisticsLayout)) {
            baseActivity!!.openActivity(EarningsActivity::class.java, null)
        }else if (v!!.id.equals(R.id.walletImg)) {
            (activity as DashboardActivity<T>).disableSwipeViewPager.setCurrentItem(2, true)
        } else if (v!!.id.equals(R.id.topupImg)) {
            baseActivity!!.openActivity(ETopUpActivity::class.java, null)
        } else if (v!!.id.equals(R.id.notificationLayout)) {
        }
        else if (v!!.id.equals(R.id.userFeedBackLayout)) {
            baseActivity!!.openActivity(SupportActivity::class.java, null)
        }
    }


    fun sendNotification(title: String, messageBody: String) {
        val channelId = NOTIFICATION_CHANNEL_ID

        val notificationBuilder = NotificationCompat.Builder(requireActivity(), channelId)
            .setSmallIcon(R.drawable.splash_logo).setSubText(title)
            .setContentTitle("My Approvals").setContentText(messageBody)
            .setAutoCancel(false)
            .setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )

        val notificationManager =
            requireContext().getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true)
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true)
            //Sets the color of Notification Light
            notificationChannel.lightColor = Color.GREEN
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )

    }

}