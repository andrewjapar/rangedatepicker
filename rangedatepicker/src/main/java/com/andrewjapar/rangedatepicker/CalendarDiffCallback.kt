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
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}