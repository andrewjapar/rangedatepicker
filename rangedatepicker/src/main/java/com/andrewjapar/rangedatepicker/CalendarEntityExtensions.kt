package com.andrewjapar.rangedatepicker

import java.util.*

internal fun CalendarEntity.Day.isTheSameDay(comparedDate: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this.date
    val comparedCalendarDate = Calendar.getInstance()
    comparedCalendarDate.time = comparedDate
    return calendar.get(Calendar.DAY_OF_YEAR) == comparedCalendarDate.get(Calendar.DAY_OF_YEAR) && calendar.get(
        Calendar.YEAR
    ) == comparedCalendarDate.get(Calendar.YEAR)
}
