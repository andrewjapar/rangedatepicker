/**
 * Copyright 2020 Andrew Japar (@andrewjapar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.andrewjapar.rangedatepicker

import java.util.*

internal sealed class CalendarEntity(
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
        val state: DateState = DateState.WEEKDAY,
        val isRange: Boolean = false
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