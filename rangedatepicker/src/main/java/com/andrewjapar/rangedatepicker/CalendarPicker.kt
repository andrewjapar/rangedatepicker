package com.andrewjapar.rangedatepicker

/**
 * Designed and developed by Andrew Japar (@andrewjapar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private val startCalendar = getInstance(timeZone, locale)
    private val endCalendar = getInstance(timeZone, locale)

    private var mCalendarData: MutableList<CalendarEntity> = mutableListOf()
    private var mStartDateSelection: SelectedDate? = null
    private var mEndDateSelection: SelectedDate? = null
    private var mPickerSelectionType = SelectionMode.RANGE

    private var mOnStartSelectedListener: (startDate: Date, label: String) -> Unit = { _, _ -> }
    private var mOnRangeSelectedListener: (startDate: Date, endDate: Date, startLabel: String, endLabel: String) -> Unit =
        { _, _, _, _ -> }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        extractAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        extractAttributes(attributeSet)
    }

    init {
        endCalendar.add(YEAR, 1)
        initAdapter()
        initListener()
    }

    fun setRangeDate(startDate: Date, endDate: Date) {
        require(startDate.time <= endDate.time) { "startDate can't be higher than endDate" }

        startCalendar.time = startDate
        endCalendar.time = endDate

        mCalendarData = buildCalendarData()
        calendarAdapter.setData(mCalendarData)
    }

    fun setMode(mode: SelectionMode) {
        mPickerSelectionType = mode
    }

    fun setOnStartSelectedListener(callback: (startDate: Date, label: String) -> Unit) {
        mOnStartSelectedListener = callback
    }

    fun setOnRangeSelectedListener(callback: (startDate: Date, endDate: Date, startLabel: String, endLabel: String) -> Unit) {
        mOnRangeSelectedListener = callback
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

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarPicker)
        mPickerSelectionType =
            SelectionMode.values()[typedArray.getInt(R.styleable.CalendarPicker_picker_type, 0)]
        typedArray.recycle()
    }

    private fun buildCalendarData(): MutableList<CalendarEntity> {
        val calendarData = mutableListOf<CalendarEntity>()
        val cal = getInstance(timeZone, locale)
        cal.time = startCalendar.time

        val monthDifference = endCalendar.totalMonthDifference(startCalendar)

        cal.set(DAY_OF_MONTH, 1)
        (0..monthDifference).forEach { _ ->
            val totalDayInAMonth = cal.getActualMaximum(DAY_OF_MONTH)
            (1..totalDayInAMonth).forEach { _ ->
                val day = cal.get(DAY_OF_MONTH)
                val dayOfWeek = cal.get(DAY_OF_WEEK)
                val dateState = if (
                    cal.isBefore(startCalendar)
                    || cal.isAfter(endCalendar)
                ) {
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

        if (item == mStartDateSelection?.day) return

        when {
            mPickerSelectionType == SelectionMode.SINGLE -> {
                if (mStartDateSelection != null) {
                    mCalendarData[mStartDateSelection!!.position] =
                        mStartDateSelection!!.day.copy(selection = SelectionType.NONE)
                }
                assignAsStartDate(item, position)
            }
            mStartDateSelection == null -> assignAsStartDate(item, position)
            mEndDateSelection == null -> {
                if (mStartDateSelection!!.position > position) {
                    mCalendarData[mStartDateSelection!!.position] =
                        mStartDateSelection!!.day.copy(selection = SelectionType.NONE)
                    assignAsStartDate(item, position)
                } else {
                    assignAsStartDate(
                        mStartDateSelection!!.day,
                        mStartDateSelection!!.position,
                        true
                    )
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
        position: Int,
        isRange: Boolean = false
    ) {
        val newItem = item.copy(selection = SelectionType.START, isRange = isRange)
        mCalendarData[position] = newItem
        mStartDateSelection = SelectedDate(newItem, position)
        if (!isRange) mOnStartSelectedListener.invoke(item.date, item.prettyLabel)
    }

    private fun assignAsEndDate(
        item: CalendarEntity.Day,
        position: Int
    ) {
        val newItem = item.copy(selection = SelectionType.END)
        mCalendarData[position] = newItem
        mEndDateSelection = SelectedDate(newItem, position)
        mOnRangeSelectedListener.invoke(
            mStartDateSelection!!.day.date,
            item.date,
            mStartDateSelection!!.day.prettyLabel,
            item.prettyLabel
        )
    }

    internal data class SelectedDate(val day: CalendarEntity.Day, val position: Int)

    enum class SelectionMode { SINGLE, RANGE }
}