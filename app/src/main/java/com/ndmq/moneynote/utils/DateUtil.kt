package com.ndmq.moneynote.utils

import com.kizitonwose.calendar.core.CalendarMonth
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun fullFormattedDate(date: Date): String {
    val format = SimpleDateFormat("dd.MM yyyy (EEE)", Locale.getDefault())
    return format.format(date)
}

fun dateMonthFormattedDate(date: Date): String {
    val format = SimpleDateFormat("dd.MM (EEE)", Locale.getDefault())
    return format.format(date)
}

fun monthYearFormattedDate(date: Date): String {
    val format = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return format.format(date)
}

fun getFirstDayOfMonth(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun getFirstDayOfMonth(calendarMonth: CalendarMonth): Date {
    val month = calendarMonth.yearMonth.month.value - 1
    val year = calendarMonth.yearMonth.year
    return getCalendar(getFirstDayOfMonth(Date())).apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }.time
}

fun getFirstDayOfMonth(yearMonth: YearMonth): Date {
    val month = yearMonth.month.value - 1
    val year = yearMonth.year
    return getCalendar(getFirstDayOfMonth(Date())).apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }.time
}

fun getStartOfDate(date: Date): Date {
    return Calendar.getInstance().apply {
        time = date
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }.time
}

fun getCalendar(date: Date): Calendar {
    return Calendar.getInstance().apply {
        time = date
    }
}

fun Date.inMonth(calendarMonth: CalendarMonth): Boolean {
    val startOfDate = getStartOfDate(getFirstDayOfMonth(calendarMonth)).time
    var month = calendarMonth.yearMonth.month.value + 1
    var year = calendarMonth.yearMonth.year
    if (month > 12) {
        month = 1
        year++
    }
    val endOfDate =
        getStartOfDate(getFirstDayOfMonth(calendarMonth.copy(YearMonth.of(year, month)))).time
    val time = this.time
    return (time in startOfDate..<endOfDate)
}

fun Date.inMonth(yearMonth: YearMonth): Boolean {
    val startOfDate = getStartOfDate(getFirstDayOfMonth(yearMonth)).time
    var month = yearMonth.month.value + 1
    var year = yearMonth.year
    if (month > 12) {
        month = 1
        year++
    }
    val endOfDate =
        getStartOfDate(getFirstDayOfMonth(YearMonth.of(year, month))).time
    val time = this.time
    return (time in startOfDate..<endOfDate)
}

fun getNextMonth(yearMonth: YearMonth): YearMonth {
    var month = yearMonth.month.value + 1
    var year = yearMonth.year
    if (month > 12) {
        month = 1
        year++
    }
    return YearMonth.of(year, month)
}

fun getPreviousMonth(yearMonth: YearMonth): YearMonth {
    var month = yearMonth.month.value - 1
    var year = yearMonth.year
    if (month < 1) {
        month = 12
        year--
    }
    return YearMonth.of(year, month)
}

fun asYearMonth(date: Date): YearMonth {
    val cal = Calendar.getInstance().apply {
        time = date
    }
    return YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)
}

fun asDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay().atZone(ZoneOffset.of("+00:00")).toInstant())
}

fun asLocalDate(date: Date): LocalDate {
    return Instant.ofEpochMilli(date.time).atZone(ZoneOffset.of("+00:00")).toLocalDate()
}

