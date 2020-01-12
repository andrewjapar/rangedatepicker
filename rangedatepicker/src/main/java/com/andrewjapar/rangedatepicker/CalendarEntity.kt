package com.andrewjapar.rangedatepicker

sealed class CalendarEntity(
    val columnCount: Int,
    val calendarType: Int,
    val selectionType: SelectionType
) {
    data class Month(val value: String) :
        CalendarEntity(MONTH_COLUMN_COUNT, CalendarType.MONTH.ordinal, SelectionType.NONE)

    object Week : CalendarEntity(WEEK_COLUMN_COUNT, CalendarType.WEEK.ordinal, SelectionType.NONE)
    data class Day(val value: String, var selection: SelectionType = SelectionType.NONE) :
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