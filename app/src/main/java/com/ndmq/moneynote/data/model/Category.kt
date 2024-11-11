package com.ndmq.moneynote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
