package com.example.gardenos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.cyclic_activity.*
import kotlinx.android.synthetic.main.planned_activity.*
import kotlinx.android.synthetic.main.planned_activity.setIrrigationDateButton
import kotlinx.android.synthetic.main.planned_activity.startIrrigationPicker
import kotlinx.android.synthetic.main.planned_activity.stopIrrigationPicker

class CyclicActivity : AppCompatActivity() {

    companion object  {
        val cyclicData = "cyclic_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cyclic_activity)

        supportActionBar!!.subtitle = "Cykliczne podlewanie"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startCyclicIrrigationPicker.setIs24HourView(true);
        stopCyclicIrrigationPicker.setIs24HourView(true);

        setIrrigationDateButton.setOnClickListener {
            val monday: CheckBox = findViewById(R.id.mondayCyclic)
            val tuesdey: CheckBox = findViewById(R.id.tuesdeyCyclic)
            val wednesdey: CheckBox = findViewById(R.id.wednesdeyCyclic)
            val thursday: CheckBox = findViewById(R.id.thursdayCyclic)
            val friday: CheckBox = findViewById(R.id.fridayCyclic)
            val saturday: CheckBox = findViewById(R.id.saturdayCyclic)
            val sunday: CheckBox = findViewById(R.id.sundayCyclic)

            val isMonday = if (monday.isChecked) 1 else 0
            val isTuesdey = if (tuesdey.isChecked) 1 else 0
            val isWednesdey = if (wednesdey.isChecked) 1 else 0
            val isThursday = if (thursday.isChecked) 1 else 0
            val isFriday = if (friday.isChecked) 1 else 0
            val isSaturday = if (saturday.isChecked) 1 else 0
            val isSunday = if (sunday.isChecked) 1 else 0

            val startMinutes = startCyclicIrrigationPicker.minute.toString()
            val startHour = startCyclicIrrigationPicker.hour.toString()

            val stopMinutes = stopCyclicIrrigationPicker.minute.toString()
            val stopHour = stopCyclicIrrigationPicker.hour.toString()

            val summary = "SCI,${isMonday},${isTuesdey},${isWednesdey},${isThursday},${isFriday},${isSaturday},${isSunday},${startHour},${startMinutes},${stopHour},${stopMinutes}"
            intent.putExtra(cyclicData, summary)

            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}