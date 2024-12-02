package com.ndmq.moneynote.presentation.calendar

import androidx.lifecycle.MutableLiveData
import com.kizitonwose.calendar.core.CalendarMonth
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.utils.asDate
import com.ndmq.moneynote.utils.inMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val selectedDate = MutableLiveData(Date())

    val currentMonth = MutableLiveData<CalendarMonth>()

    val notes = MutableLiveData<List<Note>>()

    fun fetchNotes() {
        currentMonth.value?.let { month ->
            executeTask(
                request = {
                    val startDate = asDate(month.weekDays.first().first().date)
                    val endDate = asDate(month.weekDays.last().last().date)
                    appRepository.getNotesInMonth(startDate, endDate)
                },
                onSuccess = { result ->
                    notes.value = result.filter { it.createdDate.inMonth(month) }
                }
            )
        }
    }
}