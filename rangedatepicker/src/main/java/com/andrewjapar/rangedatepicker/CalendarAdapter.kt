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

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

internal class CalendarAdapter : RecyclerView.Adapter<CalendarViewHolder>() {

    private val data: MutableList<CalendarEntity> = mutableListOf()
    var onActionListener: (CalendarEntity, Int) -> Unit = { _, _ -> }

    fun setData(newData: List<CalendarEntity>) {
        val diffCallback = CalendarDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return when (viewType) {
            CalendarType.MONTH.ordinal -> MonthViewHolder(
                parent.inflate(R.layout.calendar_month_view)
            )
            CalendarType.WEEK.ordinal -> WeekViewHolder(parent.inflate(R.layout.calendar_week_view))
            CalendarType.DAY.ordinal -> DayViewHolder(parent.inflate(R.layout.calendar_day_view))
            else -> EmptyViewHolder(parent.inflate(R.layout.calendar_empty_view))
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.onBind(data[position], onActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].calendarType
    }
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachedToRoot: Boolean = false): View =
    from(context).inflate(layoutId, this, attachedToRoot)