package com.andrewjapar.rangedatepicker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CalendarPicker : RecyclerView {

    private val timeZone = TimeZone.getDefault()
    private val locale = Locale.getDefault()

    private val calendarAdapter = CalendarAdapter()
    private var calendarEmptyData: MutableList<CalendarEntity> = mutableListOf()
    private var calendarData: MutableList<CalendarEntity> = mutableListOf()

    private var startDateSelection: SelectedDate? = null
    private var endDateSelection: SelectedDate? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    init {
        initAdapter()
        initListener()
    }

    private fun initListener() {
        calendarAdapter.onActionListener = { item, position ->
            if (item is CalendarEntity.Day) onDaySelected(item, position)
        }
    }

    private fun initAdapter() {
        layoutManager = GridLayoutManager(context, TOTAL_COLUMN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return calendarData[position].columnCount
                }
            }
        }
        adapter = calendarAdapter

        calendarEmptyData = mutableListOf(
            CalendarEntity.Month("January 2020"),
            CalendarEntity.Week,
            CalendarEntity.Empty,
            CalendarEntity.Empty,
            CalendarEntity.Empty
        )

        (1..31).forEach {
            calendarEmptyData.add(CalendarEntity.Day(it.toString()))
        }

        calendarData.addAll(calendarEmptyData)

        calendarAdapter.setData(calendarData)
    }

    private fun onDaySelected(item: CalendarEntity.Day, position: Int) {
//        if (startDateSelection != null && endDateSelection != null) {
//            resetSelection()
//        }

        when {
            startDateSelection == null -> {
                Log.d("testo", "start")
                val newItem = item.copy(selection = SelectionType.START)
                calendarData[position] = newItem
                startDateSelection = SelectedDate(newItem, position)
            }

            endDateSelection == null -> {
                Log.d("testo", "end")
                val newItem = item.copy(selection = SelectionType.END)
                endDateSelection = SelectedDate(newItem, position)

                if (startDateSelection != null) {
                    selectRange(startDateSelection?.position ?: 0, position)
                }
            }
            else -> {
                Log.d("testo", "other")
                resetSelection(item, position)

            }

        }
        calendarAdapter.setData(calendarData)
    }


    @Synchronized
    private fun resetSelection(item: CalendarEntity.Day, position: Int) {
        startDateSelection = null
        endDateSelection = null

        calendarData.clear()
        calendarData.addAll(calendarEmptyData)

        val newItem = item.copy(selection = SelectionType.START)
        calendarData[position] = newItem
        startDateSelection = SelectedDate(newItem, position)
    }

    private fun selectRange(
        startIndex: Int,
        endIndex: Int
    ) {
        calendarData.forEachIndexed { index, calendarEntity ->
            if (calendarEntity is CalendarEntity.Day) {
                val status = when {
                    startIndex == index -> SelectionType.START
                    endIndex == index -> SelectionType.END
                    index in (startIndex + 1) until endIndex -> SelectionType.BETWEEN
                    else -> SelectionType.NONE
                }
                calendarData[index] = calendarEntity.copy(selection = status)
            }
        }
    }

    data class SelectedDate(val day: CalendarEntity.Day, val position: Int)
}