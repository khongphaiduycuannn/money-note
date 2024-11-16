package com.ndmq.moneynote.presentation.setting.fixed_cost

import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_2_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_2_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_3_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_3_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_4_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_5_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_DAY
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_HALF_YEAR
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_YEAR
import com.ndmq.moneynote.data.model.FixedCost.Companion.NEVER
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.utils.minusDay
import com.ndmq.moneynote.utils.plusDay
import java.util.Calendar
import java.util.Date

object FixedCostManager {

    fun getNotesInMonth(list: List<FixedCost>, start: Date, end: Date): List<Note> {
        val notes = mutableListOf<Note>()
        list.forEach { fixedCost ->
            if (fixedCost.id == null) return@forEach

            if (fixedCost.startDate > end) return@forEach

            if (fixedCost.endDate != null && fixedCost.endDate < start) return@forEach

            val startDate = fixedCost.startDate
            if (fixedCost.startDate < start) {
                val frequency = mapFrequencyToDays(fixedCost.frequency)
                startDate.plusDay(
                    (frequency * minusDay(
                        startDate,
                        fixedCost.startDate
                    ) / frequency).toInt()
                )
            }

            var endDate = if (fixedCost.endDate == null || end < fixedCost.endDate)
                end
            else fixedCost.endDate

            if (fixedCost.frequency == NEVER) {
                endDate = Date().apply { time = fixedCost.startDate.time }
            }

            while (startDate.time <= endDate.time) {
                val calendar = Calendar.getInstance().apply { time = startDate }
                val isSat = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                val isSun = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

                var date = Date(startDate.time)
                if (fixedCost.onSaturdayAndSunday == 2) {
                    if (isSat) date = Date(date.apply { plusDay(-1) }.time)
                    if (isSun) date = Date(date.apply { plusDay(-2) }.time)
                }

                if (fixedCost.onSaturdayAndSunday == 3) {
                    if (isSat) date = Date(date.apply { plusDay(2) }.time)
                    if (isSun) date = Date(date.apply { plusDay(1) }.time)
                }

                val note = Note(
                    date,
                    fixedCost.title,
                    fixedCost.amount,
                    fixedCost.category,
                    fixedCost.id
                ).apply {
                    id = -1 * (notes.size + 1L)
                }
                notes.add(note)
                startDate.plusDay(mapFrequencyToDays(fixedCost.frequency))
            }
        }
        return notes
    }

    fun mapFrequencyToDays(frequency: Int): Int {
        return when (frequency) {
            EVERY_DAY -> 1
            EVERY_WEEK -> 7
            EVERY_2_WEEK -> 14
            EVERY_3_WEEK -> 21
            EVERY_MONTH -> 30
            EVERY_2_MONTH -> 60
            EVERY_3_MONTH -> 90
            EVERY_4_MONTH -> 120
            EVERY_5_MONTH -> 150
            EVERY_HALF_YEAR -> 180
            EVERY_YEAR -> 365
            else -> 1
        }
    }
}