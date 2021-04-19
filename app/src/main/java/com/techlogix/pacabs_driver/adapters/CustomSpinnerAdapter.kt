package com.work.mynotepad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.techlogix.pacabs_driver.R

import java.util.ArrayList

class CustomSpinnerAdapter<T>(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    internal var arrayList: ArrayList<T>
) : ArrayAdapter<T>(context, resource, textViewResourceId, arrayList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mInflater = LayoutInflater.from(parent.context)
        val view = mInflater.inflate(R.layout.spinner_item_layout, parent, false)
        val textView = view.findViewById(R.id.spinner_item_tv) as TextView
        val obj = arrayList.get(position)
        if (obj is String)
            textView.setText(obj)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context)
                .inflate(R.layout.spinner_item_layout, parent, false)
        }

        val txtTitle = convertView!!.findViewById<View>(R.id.spinner_item_tv) as TextView
        val obj = arrayList.get(position)
        if (obj is String)
            txtTitle.setText(obj)

        return convertView

    }
}
