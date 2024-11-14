package com.ndmq.moneynote.data.model.dto

data class ChartPart(
    val color: Int,
    val categoryName: String,
    val percent: Float,
    val total: Double
)

fun List<CategoryNotes>.toListChartPart(): List<ChartPart> {
    return map {
        ChartPart(
            color = it.category.tintColor,
            categoryName = it.category.categoryName,
            percent = getPercent(it).toFloat(),
            total = it.notes.sumOf { note -> note.expense }
        )
    }
}