package com.ndmq.moneynote.presentation.add_note

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.data.source.in_memory.defaultExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val notify = MutableLiveData<String>(null)

    var categories = MutableLiveData<List<Category>>()

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val selectedDate = MutableLiveData(Date())

    val selectedCategory = MutableLiveData(defaultExpenseCategory)

    fun fetchData() {
        executeTask(
            request = {
                appRepository.getCategories()
            },
            onSuccess = {
                categories.value = it
            }
        )
    }

    fun saveNote(content: String?, amount: String?) {
        val createdDate = selectedDate.value ?: Date()
        val category = selectedCategory.value ?: defaultExpenseCategory

        if (content.isNullOrBlank() || amount.isNullOrBlank()) {
            notify.value = "Save failed!"
            return
        }

        executeTask(
            request = {
                val note = Note(createdDate, content, amount.toDouble(), category.id, category)
                appRepository.addNote(note)
            },
            onSuccess = {
                notify.value = "Save successfully!"
            },
            onError = {
                notify.value = "Save failed!"
            }
        )
    }

    private fun moveToDate(dis: Int) {
        selectedDate.value?.let {
            val calendar = Calendar.getInstance().apply {
                time = it
            }
            calendar.add(Calendar.DAY_OF_YEAR, dis)
            selectedDate.value = calendar.time
        }
    }

    fun moveToPrevDate() {
        moveToDate(-1)
    }

    fun moveToNextDate() {
        moveToDate(1)
    }
}