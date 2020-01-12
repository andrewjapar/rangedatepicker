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
        calendar.add(Calendar.MONTH, 1)
        calendar_view.setRangeDate(calendar.time)
    }
}
