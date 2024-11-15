package com.ndmq.moneynote.presentation.add_note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.source.InMemoryDataSource
import com.ndmq.moneynote.data.source.defaultExpenseCategory
import java.util.Calendar
import java.util.Date

class AddNoteViewModel : ViewModel() {

    val notify = MutableLiveData<String>(null)

    var categories = listOf<Category>()

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val selectedDate = MutableLiveData(Date())

    val selectedCategory = MutableLiveData(defaultExpenseCategory)

    fun moveToPrevDate() {
        moveToDate(-1)
    }

    fun moveToNextDate() {
        moveToDate(1)
    }

    fun saveNote(content: String?, amount: String?) {
        val createdDate = selectedDate.value ?: Date()
        val category = selectedCategory.value ?: defaultExpenseCategory

        if (content.isNullOrBlank() || amount.isNullOrBlank()) {
            notify.value = "Save failed!"
            return
        }

        val note = Note(createdDate, content, amount.toDouble(), category)
        InMemoryDataSource.addNote(note)
        notify.value = "Save successfully!"
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
}