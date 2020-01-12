package com.andrewjapar.rangedatepicker

import androidx.recyclerview.widget.DiffUtil

class CalendarDiffCallback(
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