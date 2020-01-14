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

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_day_view.view.*
import kotlinx.android.synthetic.main.calendar_month_view.view.*

abstract class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit)
}

class MonthViewHolder(private val view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vMonthName }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Month) {
            name.text = item.label
        }
    }
}

open class WeekViewHolder(view: View) : CalendarViewHolder(view) {
    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
    }
}

class DayViewHolder(view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vDayName }
    private val halfLeftBg by lazy { view.vHalfLeftBg }
    private val halfRightBg by lazy { view.vHalfRightBg }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Day) {
            name.text = item.label
            when (item.selection) {
                SelectionType.START -> {
                    name.select()
                    halfLeftBg.dehighlight()
                    if (item.isRange) halfRightBg.highlight()
                    else halfRightBg.dehighlight()
                }
                SelectionType.END -> {
                    name.select()
                    halfLeftBg.highlight()
                    halfRightBg.dehighlight()
                }
                SelectionType.BETWEEN -> {
                    name.deselect()
                    halfRightBg.highlight()
                    halfLeftBg.highlight()
                }
                SelectionType.NONE -> {
                    halfLeftBg.dehighlight()
                    halfRightBg.dehighlight()
                    name.deselect()
                }
            }

            name.setTextColor(getFontColor(item))
            if (item.state != DateState.DISABLED) {
                itemView.setOnClickListener {
                    actionListener.invoke(
                        item,
                        adapterPosition
                    )
                }
            } else {
                itemView.setOnClickListener(null)
            }
        }
    }

    private fun getFontColor(item: CalendarEntity.Day): Int {
        return if (item.selection == SelectionType.START || item.selection == SelectionType.END) {
            ContextCompat.getColor(itemView.context, R.color.calendar_selected_day_font_color)
        } else {
            val color = when (item.state) {
                DateState.DISABLED -> R.color.calendar_disabled_font_color
                DateState.WEEKEND -> R.color.calendar_weekend_font_color
                else -> R.color.black
            }
            ContextCompat.getColor(itemView.context, color)
        }
    }

    private fun View.select() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.selected_day_bg)
        background = drawable
    }

    private fun View.deselect() {
        background = null
    }

    private fun View.dehighlight() {
        val color = ContextCompat.getColor(context, R.color.calendar_unselected_day)
        setBackgroundColor(color)
    }

    private fun View.highlight() {
        val color = ContextCompat.getColor(context, R.color.calendar_selected_range_bg)
        setBackgroundColor(color)
    }
}

class EmptyViewHolder(view: View) : WeekViewHolder(view)