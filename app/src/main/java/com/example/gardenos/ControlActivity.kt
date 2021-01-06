package com.example.gardenos

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_activity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsMenuItem -> {
                if (mIsConnected) {
                    val intent = Intent(applicationContext, SettingsActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Nie jesteś połączony z urządzeniem", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.plannedIrrigationMenuItem -> {
                if (mIsConnected) {
                    val intent = Intent(applicationContext, PlannedActivity::class.java)
                    startActivityForResult(intent, 1)
                } else {
                    Toast.makeText(applicationContext, "Nie jesteś połączony z urządzeniem", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            //send("p")
        }

        dscButton.setOnClickListener {
            if(mIsConnected) {
                disconnect()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val dateStart = data!!.getStringExtra(PlannedActivity.dateStart)
                val dateStop = data.getStringExtra(PlannedActivity.dateStop)
                Log.i("dateStop", dateStop.toString())
                val job = CoroutineScope(IO).launch {
                    delay(2000)
                    send(dateStart.toString())
                }
                send(dateStop.toString())
                runBlocking {
                    job.join()
                }
            }
        }

    }

    private suspend fun checkConnection() {
        if (mIsConnected) {
            coroutineScope {
                while (mIsConnected) run {
                    if (!mBluetoothAdapter.isEnabled && !mBluetoothSocket!!.isConnected) {
                        connect()
                    }
                }
            }
        }
    }

    private suspend fun readData() {
        val mmBuffer = ByteArray(1024)

        if (mIsConnected) {
            coroutineScope {
                var numBytes: Int
                while (mIsConnected && mBluetoothAdapter.isEnabled) run {
                    try {
                        numBytes = mmInStream.read(mmBuffer)
                        var receivedData: String = mmBuffer.decodeToString(endIndex = numBytes)
                        if (receivedData[0].toString() == "A") {

                            val sensorID = receivedData.slice(0..2)
                            val sensorValue = receivedData.slice(4 until receivedData.length)

                            Log.i("SENSOR", "Data from $sensorID VALUE: $sensorValue")

                            receivedData = sensorValue

                            runOnUiThread(java.lang.Runnable {
                                //outputText.append(receivedData)
                                //outputText.text = receivedData
                                if (sensorID == "A01") {
                                    outputTextSensorA01.text = sensorValue
                                } else if (sensorID == "A02") {
                                    outputTextSensorA02.text = sensorValue
                                } else if (sensorID == "A03") {
                                    outputTextSensorA03.text = sensorValue
                                } else if (sensorID == "A04") {
                                    outputTextSensorA04.text = sensorValue
                                }

                            })
                        }
                        else if (receivedData[0].toString() == "T"){
                            runOnUiThread(java.lang.Runnable {
                                dateText.text = receivedData.slice(1 until receivedData.length)
                            })
                        }
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
                mIsConnected = false
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
                val date = Date()
                val formatter = SimpleDateFormat("s,m,H,F,d,M,YY")
                val answer: String = formatter.format(date)
                val toSend = "RTC,${answer}"
                Log.i("DATE", toSend)
                send(toSend)

                connectProgressBar.visibility = View.INVISIBLE
                btConnected.visibility = View.VISIBLE
                btNotConnected.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Połączono z urządzeniem", Toast.LENGTH_SHORT).show()

                CoroutineScope(IO).launch {
                    readData()
                }

                CoroutineScope(Default).launch {
                    checkConnection()
                }

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

