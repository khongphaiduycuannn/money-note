package com.ndmq.moneynote.data.model.dto

import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.utils.getStartOfDate
import java.util.Date

data class DateNotes(
    val date: Date,
    val notes: List<Note>
) {

    val expense: Double
        get() = run {
            var result = 0.0
            notes.forEach { if (it.category.categoryType == 1) result += it.expense }
            result
        }

    val income: Double
        get() = run {
            var result = 0.0
            notes.forEach { if (it.category.categoryType == 2) result += it.expense }
            result
        }

    val total: Double
        get() = income - expense
}

fun List<Note>.toListDateNotes(): List<DateNotes> {
    return groupBy { getStartOfDate(it.createdDate) }.map {
        DateNotes(it.key, it.value)
    }
}