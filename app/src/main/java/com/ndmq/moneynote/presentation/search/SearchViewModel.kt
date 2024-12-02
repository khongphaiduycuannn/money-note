package com.ndmq.moneynote.presentation.search

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.utils.getFirstAndLastDayOfYear
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    var years = listOf<String>()

    val searchedNotes = MutableLiveData<List<Note>>()

    var searchYear: String? = null

    fun fetchYearsList() {
        executeTask(
            request = {
                appRepository.getAllYear()
            },
            onSuccess = {
                val calendar = Calendar.getInstance()
                val result: List<String> = it.map { date ->
                    calendar.apply { time = date }.get(Calendar.YEAR).toString()
                }.toSet().toList()
                years = result
            }
        )
    }

    fun fetchNotesByKeyword(keyword: String) {
        executeTask(
            request = {
                if (searchYear == null) {
                    appRepository.getNotesByKeyword(keyword)
                } else {
                    getFirstAndLastDayOfYear(searchYear!!.toInt()).let {
                        appRepository.getNotesByKeyword(keyword, it.first, it.second)
                    }
                }
            },
            onSuccess = {
                searchedNotes.value = it
            }
        )
    }
}