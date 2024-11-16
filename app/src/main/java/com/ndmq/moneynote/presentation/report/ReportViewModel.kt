package com.ndmq.moneynote.presentation.report

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.utils.asDate
import com.ndmq.moneynote.utils.inMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

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
            executeTask(
                request = {
                    val startDate = asDate(month.atDay(1))
                    val endDate = asDate(month.atDay(month.lengthOfMonth()))
                    appRepository.getNotesInMonth(startDate, endDate)
                },
                onSuccess = { result ->
                    notes.value = result.filter { it.createdDate.inMonth(month) }
                }
            )
        }
    }
}