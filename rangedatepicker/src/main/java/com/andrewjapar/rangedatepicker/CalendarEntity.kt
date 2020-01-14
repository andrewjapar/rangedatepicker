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