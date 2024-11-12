package com.ndmq.moneynote.presentation.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.source.InMemoryDataSource
import com.ndmq.moneynote.utils.inMonth
import java.time.YearMonth
import java.util.Calendar

class ReportViewModel : ViewModel() {

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val currentMonth = MutableLiveData(
        YearMonth.of(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH) + 1
        )
    )

    val notes = MutableLiveData<List<Note>>()

    fun fetchNotes() {
        currentMonth.value?.let { month ->
            notes.value = InMemoryDataSource.getNotes()
                .filter {
                    it.createdDate.inMonth(month)
                }
        }
    }
}