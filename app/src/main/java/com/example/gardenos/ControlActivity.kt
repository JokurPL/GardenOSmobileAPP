package com.example.gardenos

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.*
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.control_activity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import java.io.*
import java.lang.NullPointerException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.random.Random


class ControlActivity : AppCompatActivity() {

    companion object {
        var mMyUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        //var mMyUUID: UUID = UUID.randomUUID()
        var mIsConnected: Boolean = false
        var mBluetoothSocket: BluetoothSocket? = null

        lateinit var mBluetoothDevice: BluetoothDevice
        lateinit var mBluetoothAdapter: BluetoothAdapter
        lateinit var mAddress: String
        lateinit var mName: String
        lateinit var mmInStream: InputStream
       }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_activity)
        connectProgressBar.visibility = View.INVISIBLE

        mName = intent.getStringExtra(MainActivity.extraName).toString()
        mAddress = intent.getStringExtra(MainActivity.extraAddress).toString()

        deviceAddressView.text = mAddress
        deviceNameView.text = mName

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress)


        connectButton.setOnClickListener {
            if (!mIsConnected) {
                CoroutineScope(IO).launch {
                    connect()
                }
            }
        }

        testButton.setOnClickListener {
            send("a")
            //send("test")
            CoroutineScope(IO).launch {
                readData()
            }
        }

        dscButton.setOnClickListener {
            disconnect()
        }

    }

    private suspend fun readData() {
        val mmBuffer = ByteArray(1024)

        if (mIsConnected) {
            coroutineScope {
                var numBytes: Int
                while (mIsConnected) run {
                    try {
                        numBytes = mmInStream.read(mmBuffer)
                        runOnUiThread(java.lang.Runnable {
                            outputText.append(mmBuffer.decodeToString(endIndex = numBytes))
                        })
                    } catch (e: IOException) {
                        Log.e("IOE", e.toString())
                        this.cancel()
                    }
                }
            }
        }
        }

    private fun send(input: String) {
        if (mIsConnected) {
            if (mBluetoothSocket != null) {
                try {
                    mBluetoothSocket!!.outputStream.write(input.toByteArray())
                } catch (e: IOError) {
                    Toast.makeText(this, "Test nie powiódł się", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun connect() {
        val con = CoroutineScope(IO).async {
            try {
                mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(mMyUUID)
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                mBluetoothSocket?.connect()
                return@async true
            } catch (e: IOException) {
                Log.e("IOE", e.toString())
                //this.cancel()
                return@async false
            }
        }
        if (con.isActive) {
            runOnUiThread(java.lang.Runnable {
                btConnected.visibility = View.INVISIBLE
                btNotConnected.visibility = View.INVISIBLE
                connectProgressBar.visibility = View.VISIBLE
            })
        }
        if (con.await()) {
            runOnUiThread(java.lang.Runnable {
                if(mBluetoothSocket!!.isConnected) {
                    mIsConnected = true
                    mmInStream = mBluetoothSocket!!.inputStream
                }
                connectProgressBar.visibility = View.INVISIBLE
                btConnected.visibility = View.VISIBLE
                btNotConnected.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Połączono z urządzeniem", Toast.LENGTH_SHORT).show()
            })
        }
        else {
            runOnUiThread(java.lang.Runnable {
                mIsConnected = false
                btConnected.visibility = View.INVISIBLE
                btNotConnected.visibility = View.VISIBLE
                connectProgressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Niepołączono z urządzeniem", Toast.LENGTH_SHORT).show()
            })
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
            } finally {
                Toast.makeText(
                    applicationContext,
                    "Rozłączono z urządzeniem",
                    Toast.LENGTH_SHORT
                ).show()
                mIsConnected = false
                btConnected.visibility = View.INVISIBLE
                btNotConnected.visibility = View.VISIBLE
                connectProgressBar.visibility = View.INVISIBLE
            }
        }
    }

}

