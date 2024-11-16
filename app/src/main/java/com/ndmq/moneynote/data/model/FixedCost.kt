package com.ndmq.moneynote.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "fixed_cost")
data class FixedCost(
    val title: String,
    val amount: Double,
    val category: Category,
    val frequency: Int,
    var startDate: Date,
    val endDate: Date?,
    val onSaturdayAndSunday: Int
    /*
    * 1: Do nothing
    * 2: Set the note as before date
    * 3: Set the note as after date
    * */
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    companion object {

        const val DO_NO_THING = 1
        const val BEFORE_DATE = 2
        const val AFTER_DATE = 3

        const val NEVER = 4
        const val EVERY_DAY = 5
        const val EVERY_WEEK = 6
        const val EVERY_2_WEEK = 7
        const val EVERY_3_WEEK = 8
        const val EVERY_MONTH = 9
        const val EVERY_2_MONTH = 10
        const val EVERY_3_MONTH = 11
        const val EVERY_4_MONTH = 12
        const val EVERY_5_MONTH = 13
        const val EVERY_HALF_YEAR = 14
        const val EVERY_YEAR = 15
    }
}