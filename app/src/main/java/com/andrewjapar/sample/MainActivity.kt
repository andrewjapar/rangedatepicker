package com.andrewjapar.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstCalendarDate = Calendar.getInstance()
        firstCalendarDate.set(2019, 9, 1)

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 2)

        calendar_view.modify {
            setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
                departure_date.text = startLabel
                return_date.text = endLabel
            }

            setOnStartSelectedListener { startDate, label ->
                departure_date.text = label
                return_date.text = "-"
            }

            setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }
    }
}
