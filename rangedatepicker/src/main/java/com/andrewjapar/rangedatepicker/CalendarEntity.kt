package com.andrewjapar.rangedatepicker

import java.util.*

sealed class CalendarEntity(
    val columnCount: Int,
    val calendarType: Int,
    val selectionType: SelectionType
) {
    data class Month(val label: String) :
        CalendarEntity(MONTH_COLUMN_COUNT, CalendarType.MONTH.ordinal, SelectionType.NONE)

    object Week : CalendarEntity(WEEK_COLUMN_COUNT, CalendarType.WEEK.ordinal, SelectionType.NONE)
    data class Day(
        val label: String,
        val prettyLabel: String,
        val date: Date,
        val selection: SelectionType = SelectionType.NONE,
        val state: DateState = DateState.WEEKDAY
    ) :
        CalendarEntity(DAY_COLUMN_COUNT, CalendarType.DAY.ordinal, selection)

    object Empty :
        CalendarEntity(EMPTY_COLUMN_COUNT, CalendarType.EMPTY.ordinal, SelectionType.NONE)
}

const val TOTAL_COLUMN_COUNT = 7
const val MONTH_COLUMN_COUNT = 7
const val WEEK_COLUMN_COUNT = 7
const val DAY_COLUMN_COUNT = 1
const val EMPTY_COLUMN_COUNT = 1

enum class CalendarType {
    MONTH,
    WEEK,
    DAY,
    EMPTY
}

enum class SelectionType {
    START,
    BETWEEN,
    END,
    NONE
}

enum class DateState {
    WEEKDAY,
    DISABLED,
    WEEKEND
}