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