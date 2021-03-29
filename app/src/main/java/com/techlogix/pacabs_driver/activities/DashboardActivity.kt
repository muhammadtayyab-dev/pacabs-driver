package com.techlogix.pacabs_driver.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.techlogix.pacabs_driver.R
import com.techlogix.pacabs_driver.fragments.dashboardFragments.OnlineFragmentDirections
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initViews()
    }

    private fun initViews() {
        onlineSwitch.setOnCheckedChangeListener { view, isChecked ->
            when (isChecked) {
                true -> {
                    offlineLayout.visibility = View.GONE
                    onlineOfflineTv.text = getString(R.string.online)
                    Navigation.findNavController(this, R.id.navContainer)
                        .navigate(R.id.onlineFragment)
                }
                false -> {
                    offlineLayout.visibility = View.VISIBLE
                    onlineOfflineTv.text = getString(R.string.offline)
                    Navigation.findNavController(this, R.id.navContainer)
                        .navigate(R.id.offlineFragment)

                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}