package com.ndmq.moneynote.data.model

import androidx.room.PrimaryKey
import java.util.Date

data class Note(
    val createdDate: Date,
    val note: String,
    val expense: Double,
    val categoryId: Long?,
    val categoryType: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}