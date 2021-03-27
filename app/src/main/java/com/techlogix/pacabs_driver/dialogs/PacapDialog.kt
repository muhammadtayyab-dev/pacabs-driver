package com.techlogix.pacaps.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.techlogix.pacabs_driver.R
import kotlinx.android.synthetic.main.pacap_dialog_layout.*

class PacapDialog(context: Context) : Dialog(context) {
    init {

        setContentView(R.layout.pacap_dialog_layout)
        setCancelable(false)
        setOnCancelListener(null)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        animationView.playAnimation()
    }


    override fun dismiss() {
        super.dismiss()
        animationView.cancelAnimation()
    }
}