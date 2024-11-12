package com.ndmq.moneynote.data.model.dto

import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.Note

data class CategoryNotes(
    val category: Category,
    val notes: List<Note>
) {

    val total get() = notes.sumOf { it.expense }
}

fun List<Note>.toListCategoryNotes(): List<CategoryNotes> {
    return groupBy { it.category }.map {
        CategoryNotes(it.key, it.value)
    }
}

fun List<CategoryNotes>.getPercent(categoryNotes: CategoryNotes): Double {
    val total = sumOf { it.total }
    return if (total == 0.0) total else (categoryNotes.total / total) * 100
}