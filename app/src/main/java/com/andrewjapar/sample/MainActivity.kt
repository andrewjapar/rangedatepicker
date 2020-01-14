package com.andrewjapar.sample

import android.os.Bundle
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
            departure_date.text = startLabel
            return_date.text = endLabel
        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            departure_date.text = label
            return_date.text = "-"
        }
    }
}
