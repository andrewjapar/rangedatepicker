package com.andrewjapar.rangedatepicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.Calendar.*


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

        setData()
        calendarAdapter.setData(calendarData)
    }

    private fun setData() {
        val cal = getInstance(timeZone, locale)
        val currentDay = cal.get(DAY_OF_MONTH)
        val currentMonth = cal.get(MONTH)
        val currentYear = cal.get(YEAR)

        cal.set(DAY_OF_MONTH, 1)
        (currentMonth..(currentMonth + 11)).forEach {

            val month = cal.getDisplayName(MONTH, LONG, locale) ?: ""
            val year = cal.get(YEAR)

            val numberOfDayOfMonth = cal.getActualMaximum(DAY_OF_MONTH)
            (1..numberOfDayOfMonth).forEach {
                val day = cal.get(DAY_OF_MONTH)
                val dayOfWeek = cal.get(DAY_OF_WEEK)
                when (day) {
                    cal.firstDayOfWeek -> {
                        calendarData.add(CalendarEntity.Month("$month $year"))
                        calendarData.add(CalendarEntity.Week)
                        calendarData.addAll(createStartEmptyView(dayOfWeek))
                        calendarData.add(CalendarEntity.Day(day.toString()))
                    }
                    numberOfDayOfMonth -> {
                        calendarData.add(CalendarEntity.Day(day.toString()))
                        calendarData.addAll(createEndEmptyView(dayOfWeek))
                    }
                    else -> {
                        calendarData.add(CalendarEntity.Day(day.toString()))
                    }
                }
                cal.add(DATE, 1)
            }
        }
    }

    private fun createStartEmptyView(dayOfWeek: Int): List<CalendarEntity.Empty> {
        val numberOfEmptyView = when (dayOfWeek) {
            MONDAY -> 1
            TUESDAY -> 2
            WEDNESDAY -> 3
            THURSDAY -> 4
            FRIDAY -> 5
            SATURDAY -> 6
            else -> 0
        }

        val listEmpty = mutableListOf<CalendarEntity.Empty>()
        repeat((0..numberOfEmptyView).count()) { listEmpty.add(CalendarEntity.Empty) }
        return listEmpty
    }

    private fun createEndEmptyView(dayOfWeek: Int): List<CalendarEntity.Empty> {
        val numberOfEmptyView = when (dayOfWeek) {
            SUNDAY -> 6
            MONDAY -> 5
            TUESDAY -> 4
            WEDNESDAY -> 3
            THURSDAY -> 2
            FRIDAY -> 1
            else -> 6
        }

        val listEmpty = mutableListOf<CalendarEntity.Empty>()
        repeat((0..numberOfEmptyView).count()) { listEmpty.add(CalendarEntity.Empty) }
        return listEmpty
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