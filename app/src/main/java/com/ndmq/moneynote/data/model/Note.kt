package com.ndmq.moneynote.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "note")
data class Note(
    var createdDate: Date,
    var note: String,
    var expense: Double,
    var categoryId: Long?,
    var category: Category,
    var fixedCostId: Long? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}