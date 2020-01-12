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
    private val mCalendar = getInstance(timeZone, locale)

    private var mCalendarData: MutableList<CalendarEntity> = mutableListOf()
    private var mStartDateSelection: SelectedDate? = null
    private var mEndDateSelection: SelectedDate? = null

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

    fun setRangeDate(date: Date) {
        mCalendar.time = date
        mCalendarData = buildCalendarData()
        calendarAdapter.setData(mCalendarData)
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
                    return mCalendarData[position].columnCount
                }
            }
        }
        adapter = calendarAdapter
        mCalendarData = buildCalendarData()
        calendarAdapter.setData(mCalendarData)
    }

    private fun buildCalendarData(): MutableList<CalendarEntity> {
        val calendarData = mutableListOf<CalendarEntity>()

        val cal = mCalendar
        val currentDay = cal.get(DAY_OF_MONTH)
        val currentMonth = cal.get(MONTH)
        val currentYear = cal.get(YEAR)

        cal.set(DAY_OF_MONTH, 1)
        (currentMonth..(currentMonth + 11)).forEach { _ ->

            val totalDayInAMonth = cal.getActualMaximum(DAY_OF_MONTH)
            (1..totalDayInAMonth).forEach { _ ->
                val day = cal.get(DAY_OF_MONTH)
                val dayOfWeek = cal.get(DAY_OF_WEEK)
                val dateState = if (cal.isBefore(currentDay, currentMonth, currentYear)) {
                    DateState.DISABLED
                } else {
                    DateState.WEEKDAY
                }
                when (day) {
                    cal.firstDayOfWeek -> {
                        calendarData.add(CalendarEntity.Month(cal.toPrettyMonthString()))
                        calendarData.add(CalendarEntity.Week)
                        calendarData.addAll(createStartEmptyView(dayOfWeek))
                        calendarData.add(
                            CalendarEntity.Day(
                                day.toString(),
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState
                            )
                        )
                    }
                    totalDayInAMonth -> {
                        calendarData.add(
                            CalendarEntity.Day(
                                day.toString(),
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState
                            )
                        )
                        calendarData.addAll(createEndEmptyView(dayOfWeek))
                    }
                    else -> {
                        calendarData.add(
                            CalendarEntity.Day(
                                day.toString(),
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState
                            )
                        )
                    }
                }
                cal.add(DATE, 1)
            }
        }

        return calendarData
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
            mStartDateSelection == null -> assignAsStartDate(item, position)

            mEndDateSelection == null -> {
                if (mStartDateSelection!!.position > position) {
                    mCalendarData[mStartDateSelection!!.position] =
                        mStartDateSelection!!.day.copy(selection = SelectionType.NONE)
                    assignAsStartDate(item, position)
                } else {
                    assignAsEndDate(item, position)
                    highlightDateBetween(mStartDateSelection!!.position, position)
                }
            }

            else -> {
                resetSelection()
                assignAsStartDate(item, position)
            }
        }

        calendarAdapter.setData(mCalendarData)
    }

    private fun resetSelection() {
        val startDatePosition = mStartDateSelection?.position
        val endDatePosition = mEndDateSelection?.position

        if (startDatePosition != null && endDatePosition != null) {
            (startDatePosition..endDatePosition).forEach {
                val entity = mCalendarData[it]
                if (entity is CalendarEntity.Day)
                    mCalendarData[it] = entity.copy(selection = SelectionType.NONE)
            }
        }
        mEndDateSelection = null
    }


    private fun highlightDateBetween(
        startIndex: Int,
        endIndex: Int
    ) {
        ((startIndex + 1) until endIndex).forEach {
            val entity = mCalendarData[it]
            if (entity is CalendarEntity.Day) {
                mCalendarData[it] = entity.copy(selection = SelectionType.BETWEEN)
            }
        }
    }

    private fun assignAsStartDate(
        item: CalendarEntity.Day,
        position: Int
    ) {
        val newItem = item.copy(selection = SelectionType.START)
        mCalendarData[position] = newItem
        mStartDateSelection = SelectedDate(newItem, position)
    }

    private fun assignAsEndDate(
        item: CalendarEntity.Day,
        position: Int
    ) {
        val newItem = item.copy(selection = SelectionType.END)
        mCalendarData[position] = newItem
        mEndDateSelection = SelectedDate(newItem, position)
    }

    data class SelectedDate(val day: CalendarEntity.Day, val position: Int)
}