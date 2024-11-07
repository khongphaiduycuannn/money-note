package com.ndmq.moneynote.presentation.add_note

import android.app.DatePickerDialog
import android.widget.DatePicker
import java.util.Calendar
import java.util.Date

class DatePickerListener : DatePickerDialog.OnDateSetListener {

    private var onDateSelected: (Date) -> Unit = {}

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        onDateSelected(calendar.time)
    }

    fun setOnDateSelected(func: (Date) -> Unit) {
        onDateSelected = func
    }
}