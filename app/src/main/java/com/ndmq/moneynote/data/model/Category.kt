package com.ndmq.moneynote.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category")
data class Category(
    val iconResource: Int,
    val tintColor: Int,
    val categoryName: String,
    val categoryType: Int
    /*
    * 1: Expense
    * 2: Income
    */
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
