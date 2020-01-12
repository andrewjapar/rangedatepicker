package com.andrewjapar.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.add(Calendar.MONTH, 0)
        val calendar2 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar2.time = calendar.time
        calendar2.add(Calendar.YEAR, 1)
        calendar_view.setRangeDate(calendar.time, calendar2.time)

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            Toast.makeText(this, "$startLabel - $endLabel", Toast.LENGTH_SHORT).show()
        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            Toast.makeText(this, label, Toast.LENGTH_SHORT).show()
        }
    }
}
