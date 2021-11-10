package com.techlogix.pacabs_driver.dialogs

import android.content.ComponentCallbacks
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techlogix.pacabs_driver.R
import com.techlogix.pacaps.dialogs.AlertDialogCallback
import kotlinx.android.synthetic.main.bottomsheet_end_ride.*

class EndRideBottomSheetDialogFragment(context: Context,var callbacks: AlertDialogCallback) : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = LayoutInflater.from(context)
            .inflate(R.layout.bottomsheet_end_ride, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        endRideBtn.setOnClickListener { dismiss() }
        
    }

    override fun dismiss() {
        super.dismiss()
        callbacks.onDissmiss(true)
    }
}