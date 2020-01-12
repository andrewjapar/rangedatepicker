package com.andrewjapar.rangedatepicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CalendarPicker : RecyclerView {

    private val timeZone = TimeZone.getDefault()
    private val locale = Locale.getDefault()

    private val calendarAdapter = CalendarAdapter()
    private var calendarData: MutableList<CalendarEntity> = mutableListOf()

    private var startDateSelection: SelectedDate? = null
    private var endDateSelection: SelectedDate? = null

    var onRangeSelectedListener: (startDate: Date, endDate: Date) -> Unit = { _, _ -> }

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

        calendarData = mutableListOf(
            CalendarEntity.Month("January 2020"),
            CalendarEntity.Week,
            CalendarEntity.Empty,
            CalendarEntity.Empty,
            CalendarEntity.Empty
        )

        (1..31).forEach {
            calendarData.add(CalendarEntity.Day(it.toString()))
        }

        calendarAdapter.setData(calendarData)
    }

    private fun onDaySelected(item: CalendarEntity.Day, position: Int) {

        when {
            startDateSelection == null -> assignAsStartDate(item, position)

            endDateSelection == null -> {
                if (startDateSelection!!.position > position) {
                    calendarData[startDateSelection!!.position] =
                        startDateSelection!!.day.copy(selection = SelectionType.NONE)
                    assignAsStartDate(item, position)
                } else {
                    assignAsEndDate(item, position)
                    selectRange(startDateSelection!!.position, position)
                }
            }

            else -> resetSelection(item, position)
        }

        calendarAdapter.setData(calendarData)
    }

    private fun resetSelection(item: CalendarEntity.Day, position: Int) {
        val startDatePosition = startDateSelection?.position
        val endDatePosition = endDateSelection?.position

        if (startDatePosition != null && endDatePosition != null) {
            (startDatePosition..endDatePosition).forEach {
                val entity = calendarData[it]
                if (entity is CalendarEntity.Day)
                    calendarData[it] = entity.copy(selection = SelectionType.NONE)
            }
        }

        val newItem = item.copy(selection = SelectionType.START)
        calendarData[position] = newItem
        startDateSelection = SelectedDate(newItem, position)
        endDateSelection = null
    }


    private fun selectRange(
        startIndex: Int,
        endIndex: Int
    ) {
        ((startIndex + 1) until endIndex).forEach {
            val entity = calendarData[it]
            if (entity is CalendarEntity.Day) {
                calendarData[it] = entity.copy(selection = SelectionType.BETWEEN)
            }
        }
    }

    private fun assignAsStartDate(
        item: CalendarEntity.Day,
        position: Int
    ) {
        val newItem = item.copy(selection = SelectionType.START)
        calendarData[position] = newItem
        startDateSelection = SelectedDate(newItem, position)
    }

    private fun assignAsEndDate(
        item: CalendarEntity.Day,
        position: Int
    ) {
        val newItem = item.copy(selection = SelectionType.END)
        calendarData[position] = newItem
        endDateSelection = SelectedDate(newItem, position)
    }

    data class SelectedDate(val day: CalendarEntity.Day, val position: Int)
}