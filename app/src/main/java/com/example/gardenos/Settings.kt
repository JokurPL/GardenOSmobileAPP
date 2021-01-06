package com.example.gardenos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.textservice.TextInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        startIrrigationPicker.setIs24HourView(true);
        finishIrrigationPicker.setIs24HourView(true);

        irrigationDatePicker.minDate = System.currentTimeMillis()
        setIrrigationDateButton.setOnClickListener {
            val intent = Intent()

            val seconds = 0

            val calendar = Calendar.getInstance()

            var startMinutes = startIrrigationPicker.minute.toString()
            var startHour = startIrrigationPicker.hour.toString()
            var startMonth = (irrigationDatePicker.month + 1).toString()
            var startDay = irrigationDatePicker.dayOfMonth.toString()

            val startYear = irrigationDatePicker.year.toString()

            intent.putExtra("date", "ATI,${seconds},${startMinutes},${startHour},0,${startDay},${startMonth},${startYear[2]}${startYear[3]}")
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}