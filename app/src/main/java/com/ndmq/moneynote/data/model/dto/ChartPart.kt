package com.ndmq.moneynote.data.model.dto

data class ChartPart(
    val color: Int,
    val categoryName: String,
    val percent: Float
)

fun List<CategoryNotes>.toListChartPart(): List<ChartPart> {
    return map {
        ChartPart(
            color = it.category.tintColor,
            categoryName = it.category.categoryName,
            percent = getPercent(it).toFloat()
        )
    }
}