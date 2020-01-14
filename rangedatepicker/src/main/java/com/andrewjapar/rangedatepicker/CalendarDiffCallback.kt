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

import androidx.recyclerview.widget.DiffUtil

internal class CalendarDiffCallback(
    private val oldList: List<CalendarEntity>,
    private val newList: List<CalendarEntity>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]::class == newList[newItemPosition]::class
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (oldList[oldItemPosition] is CalendarEntity.Day && newList[newItemPosition] is CalendarEntity.Day) {
            val oldDay = oldList[oldItemPosition] as CalendarEntity.Day
            val newDay = newList[newItemPosition] as CalendarEntity.Day
            oldDay.selection == newDay.selection && oldDay.isRange == newDay.isRange
        } else {
            oldList[oldItemPosition].selectionType == newList[newItemPosition].selectionType
        }
    }
}