package com.ndmq.moneynote.presentation.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendar.core.CalendarMonth
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.source.InMemoryDataSource
import com.ndmq.moneynote.utils.inMonth
import java.util.Date

class CalendarViewModel : ViewModel() {

    val selectedDate = MutableLiveData(Date())

    val currentMonth = MutableLiveData<CalendarMonth>()

    val notes = MutableLiveData<List<Note>>()

    fun fetchNotes() {
        currentMonth.value?.let { month ->
            notes.value = InMemoryDataSource.getNotes().filter {
                it.createdDate.inMonth(month)
            }
        }
    }
}