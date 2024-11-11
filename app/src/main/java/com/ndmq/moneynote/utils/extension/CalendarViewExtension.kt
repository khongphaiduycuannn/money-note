package com.ndmq.moneynote.utils.extension

import com.kizitonwose.calendar.view.CalendarView
import com.ndmq.moneynote.utils.asDate
import com.ndmq.moneynote.utils.asYearMonth
import com.ndmq.moneynote.utils.getCalendar
import java.util.Calendar

private fun CalendarView.scrollToAddMonth(add: Int) {
    findLastVisibleDay()?.date?.let { localDate ->
        val nextMonth = asYearMonth(getCalendar(asDate(localDate)).apply {
            add(Calendar.DAY_OF_MONTH, -10)

            val currentMonth = get(Calendar.MONTH)
            var newMonth = currentMonth + add
            if (newMonth > 11) {
                newMonth = 0
                add(Calendar.YEAR, 1)
            } else if (newMonth < 0) {
                newMonth = 11
                add(Calendar.YEAR, -1)
            }
            set(Calendar.MONTH, newMonth)
        }.time)
        smoothScrollToMonth(nextMonth)
    }
}

fun CalendarView.scrollToNextMonth() {
    scrollToAddMonth(1)
}

fun CalendarView.scrollToPrevMonth() {
    scrollToAddMonth(-1)
}

