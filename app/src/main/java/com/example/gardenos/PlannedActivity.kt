package com.example.gardenos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.planned_activity.*

class PlannedActivity : AppCompatActivity() {

    companion object {
        val dateStart: String = "date_start_irrigation"
        val dateStop: String = "date_stop_irrigation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.planned_activity)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.subtitle = "Planowane podlewanie"

        startIrrigationPicker.setIs24HourView(true);
        stopIrrigationPicker.setIs24HourView(true);

        irrigationDatePicker.minDate = System.currentTimeMillis()
        stopIrrigationDatePicker.minDate = System.currentTimeMillis()

        setIrrigationDateButton.setOnClickListener {
            val intent = Intent()
            val seconds = 0

            val startMinutesStart = startIrrigationPicker.minute.toString()
            val startHourStart = startIrrigationPicker.hour.toString()
            val startMonthStart = (irrigationDatePicker.month + 1).toString()
            val startDayStart = irrigationDatePicker.dayOfMonth.toString()
            val startYearStart = irrigationDatePicker.year.toString()

            val startMinutesStop = stopIrrigationPicker.minute.toString()
            val startHourStop = stopIrrigationPicker.hour.toString()
            val startMonthStop = (stopIrrigationDatePicker.month + 1).toString()
            val startDayStop = stopIrrigationDatePicker.dayOfMonth.toString()
            val startYearStop = stopIrrigationDatePicker.year.toString()

            intent.putExtra(dateStart, "ATS,${seconds},${startMinutesStart},${startHourStart},0,${startDayStart},${startMonthStart},${startYearStart[2]}${startYearStart[3]}")
            intent.putExtra(dateStop, "ATF,${seconds},${startMinutesStop},${startHourStop},0,${startDayStop},${startMonthStop},${startYearStop[2]}${startYearStop[3]}")

            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}