package com.example.gardenos

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DeviceAdapter(var mCtx: Context, var resources: Int, var items: List<BluetoothDevice>): ArrayAdapter<BluetoothDevice>(mCtx, resources, items) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)

        val nameView: TextView = view.findViewById(android.R.id.text1)
        val addresView: TextView = view.findViewById(android.R.id.text2)

        val mItem: BluetoothDevice = items[position]
        nameView.text = mItem.name
        addresView.text = mItem.address

        return view
    }
}