package com.techlogix.pacaps.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.techlogix.pacabs_driver.R
import kotlinx.android.synthetic.main.success_dialog_layout.*

public class ErrorSuccessDialog(context: Context,
                         title: String,
                         msg: String,
                         alertDialogcallbacl: AlertDialogCallback) : Dialog(context,R.style.ErrorDialog),
    View.OnClickListener {
    var altertDialogCallback: AlertDialogCallback? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.success_dialog_layout)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        window!!.setBackgroundDrawableResource(R.color.transparent)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        lbl_success_title.setText(title)
        lbl_success_desc.setText(msg)
        btn_ok.setOnClickListener(this)
        this.altertDialogCallback = alertDialogcallbacl
    }

    override fun onClick(view: View?) {
        if (view!!.id == R.id.btn_ok) {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        altertDialogCallback?.onDissmiss(true)

    }
}