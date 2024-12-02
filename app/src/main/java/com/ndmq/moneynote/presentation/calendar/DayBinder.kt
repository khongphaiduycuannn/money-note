package com.ndmq.moneynote.presentation.calendar

import android.view.View
import androidx.core.content.ContextCompat.getColor
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.model.dto.toListDateNotes
import com.ndmq.moneynote.utils.asDate
import com.ndmq.moneynote.utils.getStartOfDate
import java.time.LocalDate
import java.util.Date

class DayBinder(
    private val calendarView: CalendarView
) : MonthDayBinder<DayViewContainer> {

    private var selectedDate: LocalDate = LocalDate.now()

    private var noteData = mutableMapOf<Date, Pair<Double, Double>>()
    /*
        .first: expense
        .second: income
    */

    private var onDateSelected: (LocalDate) -> Unit = {}

    override fun bind(container: DayViewContainer, data: CalendarDay) {
        with(container) {
            tvDate.text = data.date.dayOfMonth.toString()

            if (data.position == DayPosition.MonthDate) {
                tvDate.setTextColor(getColor(calendarView.context, R.color.defaultTextColor))
                llDateItem.setBackgroundResource(R.drawable.bg_item_calendar_date_in_month)
            } else {
                tvDate.setTextColor(getColor(calendarView.context, R.color.disableTextColor))
                llDateItem.setBackgroundResource(R.drawable.bg_item_calendar_date_out_month)
            }

            if (data.date == selectedDate) {
                llDateItem.setBackgroundResource(R.drawable.bg_item_calendar_date_selected_date)
            }

            val dateData = noteData[getStartOfDate(asDate(data.date))]
            if (dateData == null) {
                tvExpense.text = ""
                tvIncome.text = ""
            } else {
                tvExpense.text = dateData.first.toString()
                tvIncome.text = dateData.second.toString()
            }

            llDateItem.setOnClickListener {
                onDateSelected(data.date)
            }
        }
    }

    override fun create(view: View): DayViewContainer {
        return DayViewContainer(view)
    }

    fun setNoteData(data: List<Note>) {
        noteData.clear()
        data.toListDateNotes().forEach {
            noteData[it.date] = it.expense to it.income
        }
        calendarView.notifyCalendarChanged()
    }

    fun setOnDateSelected(func: (LocalDate) -> Unit) {
        onDateSelected = func
        calendarView.notifyCalendarChanged()
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
        calendarView.notifyCalendarChanged()
    }
}