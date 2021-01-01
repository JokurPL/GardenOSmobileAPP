package com.example.gardenos

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var mPairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val extraAddress: String = "Device_address"
        val extraName: String = "Device_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Te urządzenie nie wspiera Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBluetoothIntent)
        }

        select_device_refresh.setOnClickListener { pairedDeviceList() }

    }

    private fun pairedDeviceList() {
        mPairedDevices = mBluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (mPairedDevices.isNotEmpty()) {

            for (device: BluetoothDevice in mPairedDevices) {
                list.add(device)
                Log.i("device", "" + device)
            }
        } else {
            Toast.makeText(this, "Nie znaleziono sparowanych urządzeń", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener{_, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val macAddress: String = device.address
            val nameDevice: String = device.name

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(extraAddress, macAddress)
            intent.putExtra(extraName, nameDevice)

            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (mBluetoothAdapter!!.isEnabled) {
                    Toast.makeText(this, "Bluetooth został włączony", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Bluetooth został wyłączony", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Włączenie Bluetooth zostalo przerwane", Toast.LENGTH_SHORT).show()
            }
        }
    }

}