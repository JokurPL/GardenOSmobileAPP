package com.example.gardenos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.planned_activity.*

class CyclicActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cyclic_activity)

        supportActionBar!!.subtitle = "Cykliczne podlewanie"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startIrrigationPicker.setIs24HourView(true);
        stopIrrigationPicker.setIs24HourView(true);

        val dayAmountSpinner: Spinner = findViewById(R.id.dayAmountCyclicIrrigation)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.day_amount_cyclic_irrigation,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dayAmountSpinner.adapter = adapter
        dayAmountSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val dayOfWeekSpinner1: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation1)
        val dayOfWeekSpinner2: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation2)
        val dayOfWeekSpinner3: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation3)
        val dayOfWeekSpinner4: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation4)
        val dayOfWeekSpinner5: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation5)
        val dayOfWeekSpinner6: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation6)
        val dayOfWeekSpinner7: Spinner = findViewById(R.id.dayOfWeekCyclicIrrigation7)

        Toast.makeText(this, "Wybrałeś: ${parent?.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()

        when (parent?.getItemAtPosition(position)) {
            "1" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.GONE
                dayOfWeekSpinner3.visibility = View.GONE
                dayOfWeekSpinner4.visibility = View.GONE
                dayOfWeekSpinner5.visibility = View.GONE
                dayOfWeekSpinner6.visibility = View.GONE
                dayOfWeekSpinner7.visibility = View.GONE
            }
            "2" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.GONE
                dayOfWeekSpinner4.visibility = View.GONE
                dayOfWeekSpinner5.visibility = View.GONE
                dayOfWeekSpinner6.visibility = View.GONE
                dayOfWeekSpinner7.visibility = View.GONE
            }
            "3" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.VISIBLE
                dayOfWeekSpinner4.visibility = View.GONE
                dayOfWeekSpinner5.visibility = View.GONE
                dayOfWeekSpinner6.visibility = View.GONE
                dayOfWeekSpinner7.visibility = View.GONE
            }
            "4" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.VISIBLE
                dayOfWeekSpinner4.visibility = View.VISIBLE
                dayOfWeekSpinner5.visibility = View.GONE
                dayOfWeekSpinner6.visibility = View.GONE
                dayOfWeekSpinner7.visibility = View.GONE
            }
            "5" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.VISIBLE
                dayOfWeekSpinner4.visibility = View.VISIBLE
                dayOfWeekSpinner5.visibility = View.VISIBLE
                dayOfWeekSpinner6.visibility = View.GONE
                dayOfWeekSpinner7.visibility = View.GONE

            }
            "6" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.VISIBLE
                dayOfWeekSpinner4.visibility = View.VISIBLE
                dayOfWeekSpinner5.visibility = View.VISIBLE
                dayOfWeekSpinner6.visibility = View.VISIBLE
                dayOfWeekSpinner7.visibility = View.GONE
            }
            "7" -> {
                dayOfWeekSpinner1.visibility = View.VISIBLE
                dayOfWeekSpinner2.visibility = View.VISIBLE
                dayOfWeekSpinner3.visibility = View.VISIBLE
                dayOfWeekSpinner4.visibility = View.VISIBLE
                dayOfWeekSpinner5.visibility = View.VISIBLE
                dayOfWeekSpinner6.visibility = View.VISIBLE
                dayOfWeekSpinner7.visibility = View.GONE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TO DO
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}