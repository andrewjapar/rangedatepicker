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

import androidx.recyclerview.widget.DiffUtil

internal class CalendarDiffCallback : DiffUtil.ItemCallback<CalendarEntity>() {

    override fun areItemsTheSame(oldItem: CalendarEntity, newItem: CalendarEntity): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: CalendarEntity, newItem: CalendarEntity): Boolean {
        return if (oldItem is CalendarEntity.Day && newItem is CalendarEntity.Day) {
            oldItem.selection == newItem.selection && oldItem.isRange == newItem.isRange
        } else {
            oldItem.selectionType == newItem.selectionType
        }
    }
}