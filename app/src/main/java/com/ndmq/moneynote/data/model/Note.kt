package com.ndmq.moneynote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "note")
data class Note(
    var createdDate: Date,
    var note: String,
    var expense: Double,
    var category: Category,
    var fixedCostId: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}