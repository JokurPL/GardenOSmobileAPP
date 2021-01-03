package com.example.gardenos

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var mPairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val extraAddress: String = "Device_address"
        val extraName: String = "Device_name"
    }

    @SuppressLint("ResourceAsColor")
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
        else {
            pairedDeviceList()
        }

        swiperefresh.setColorSchemeColors(R.color.purple_200)

        swiperefresh.setOnRefreshListener {
            if (!mBluetoothAdapter!!.isEnabled) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(enableBluetoothIntent)
            }
            pairedDeviceList()
        }
    }


    private fun pairedDeviceList() {
        val list = mutableListOf<BluetoothDevice>()

        mPairedDevices = mBluetoothAdapter!!.bondedDevices
        if (mPairedDevices.isNotEmpty()) {

            for (device: BluetoothDevice in mPairedDevices) {
                list.add(device)
                Log.i("device", "" + device)
            }
        } else {
            Toast.makeText(this, "Nie znaleziono sparowanych urządzeń", Toast.LENGTH_SHORT).show()
        }

        val adapter = DeviceAdapter(this, android.R.layout.simple_list_item_2,  list)

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
        swiperefresh.isRefreshing = false
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