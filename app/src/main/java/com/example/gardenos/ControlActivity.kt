package com.example.gardenos

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_activity.*
import java.io.IOError
import java.io.IOException
import java.util.*


class ControlActivity: AppCompatActivity() {

    companion object {
        var mMyUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        //var mMyUUID: UUID = UUID.randomUUID()
        var mIsConnected: Boolean = false
        var mBluetoothSocket: BluetoothSocket? = null

        lateinit var mBluetoothDevice: BluetoothDevice
        lateinit var mBluetoothAdapter: BluetoothAdapter
        lateinit var mAddress: String
        lateinit var mName: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_activity)

        mName = intent.getStringExtra(MainActivity.extraName).toString()
        mAddress = intent.getStringExtra(MainActivity.extraAddress).toString()

        deviceAddressView.text = mAddress
        deviceNameView.text = mName

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress)

        connect()

        dscButton.setOnClickListener {
            disconnect()
        }

        testButton.setOnClickListener {
            send("a")
        }
    }

    private fun send(input: String) {
        if (mIsConnected) {
            if (mBluetoothSocket != null) {
                try {
                    mBluetoothSocket!!.outputStream.write(input.toByteArray())
                }
                catch (e: IOError) {
                    Toast.makeText(this, "Test nie powiódł się", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun connect() {
        try {
            mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(mMyUUID)
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

            mBluetoothSocket?.connect()
            Toast.makeText(applicationContext, "Urządzenie zostało połączone", Toast.LENGTH_SHORT).show()
            mIsConnected = true
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "Połączenie nie udało się", Toast.LENGTH_SHORT).show()
            Log.i("IOE", e.toString())
        }
    }

    private fun disconnect() {
        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket!!.close()
                mBluetoothSocket = null

            } catch (e: IOException) {
                Toast.makeText(this, "Coś poszło nie tak", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
        finish()
        Toast.makeText(applicationContext, "Urządzenie zostało rozłączone", Toast.LENGTH_SHORT).show()
    }

}

