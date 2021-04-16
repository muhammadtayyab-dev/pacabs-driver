package com.techlogix.pacabs_driver.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.techlogix.pacabs_driver.R
import com.techlogix.pacaps.dialogs.AlertDialogCallback
import kotlinx.android.synthetic.main.enter_otp_dialog.*

class EnterOTPDialog(context: Context, alertDialogcallbacl: AlertDialogCallback) : Dialog(context) {
    var altertDialogCallback: AlertDialogCallback? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(R.layout.enter_otp_dialog)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window!!.attributes)
        lp.width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
//        lp.height = (context.resources.displayMetrics.heightPixels * 0.70).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        window!!.setBackgroundDrawableResource(R.color.transparent)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        this.altertDialogCallback = alertDialogcallbacl
        doneBtn.setOnClickListener { dismiss() }
    }

    override fun dismiss() {
        super.dismiss()
        altertDialogCallback?.onDissmiss()
    }
}