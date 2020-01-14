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

import java.util.*
import java.util.Calendar.*

fun Calendar.toPrettyMonthString(
    style: Int = Calendar.LONG,
    locale: Locale = Locale.getDefault()
): String {
    val month = getDisplayName(MONTH, style, locale)
    val year = get(YEAR).toString()
    return if (month == null) throw IllegalStateException("Cannot get pretty name")
    else "$month $year"
}

fun Calendar.toPrettyDateString(locale: Locale = Locale.getDefault()): String {
    val day = get(DAY_OF_MONTH).toString()
    return "$day ${this.toPrettyMonthString(Calendar.SHORT, locale)}"
}

fun Calendar.isBefore(otherCalendar: Calendar): Boolean {
    return get(YEAR) == otherCalendar.get(YEAR)
            && get(MONTH) == otherCalendar.get(MONTH)
            && get(DAY_OF_MONTH) < otherCalendar.get(DAY_OF_MONTH)
}

fun Calendar.isAfter(otherCalendar: Calendar): Boolean {
    return get(YEAR) == otherCalendar.get(YEAR)
            && get(MONTH) == otherCalendar.get(MONTH)
            && get(DAY_OF_MONTH) > otherCalendar.get(DAY_OF_MONTH)
}

fun Calendar.totalMonthDifference(startCalendar: Calendar): Int {
    val yearDiff = get(YEAR) - startCalendar.get(YEAR)
    val monthDiff = get(MONTH) - startCalendar.get(MONTH)

    return monthDiff + (yearDiff * 12)
}