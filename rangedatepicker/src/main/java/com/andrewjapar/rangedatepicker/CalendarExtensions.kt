package com.andrewjapar.rangedatepicker

import java.util.*

internal fun Date.isTheSameDay(comparedDate: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.withTime(this)
    val comparedCalendarDate = Calendar.getInstance()
    comparedCalendarDate.withTime(comparedDate)
    return calendar.get(Calendar.DAY_OF_YEAR) == comparedCalendarDate.get(Calendar.DAY_OF_YEAR) && calendar.get(
        Calendar.YEAR
    ) == comparedCalendarDate.get(Calendar.YEAR)
}

internal fun Calendar.withTime(date: Date) {
    clear()
    time = date
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}