package com.andrewjapar.rangedatepicker

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

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Day) {
            name.text = item.label
            name.setBackgroundColor(getColor(item))
            itemView.setOnClickListener {
                actionListener.invoke(
                    item,
                    adapterPosition
                )
            }
        }
    }

    private fun getColor(item: CalendarEntity): Int {
        val color = when (item.selectionType) {
            SelectionType.NONE -> R.color.calendar_unselected_day
            SelectionType.START, SelectionType.END -> R.color.calendar_selected_day_bg
            SelectionType.BETWEEN -> R.color.calendar_selected_range_bg
        }
        return ContextCompat.getColor(itemView.context, color)
    }
}

class EmptyViewHolder(view: View) : WeekViewHolder(view)