package com.ndmq.moneynote.presentation.edit_note

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
class EditNoteViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val notify = MutableLiveData<String>(null)

    var id: Long? = null

    var categories = MutableLiveData<List<Category>>()

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val selectedDate = MutableLiveData(Date())

    val selectedCategory = MutableLiveData(defaultExpenseCategory)

    var fixedCostId: Long? = null

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

    fun saveNote(content: String?, amount: String?, doOnComplete: () -> Unit) {
        if (id == null) {
            addNote(content, amount, doOnComplete)
        } else {
            updateNote(content, amount, doOnComplete)
        }
    }

    private fun addNote(content: String?, amount: String?, doOnComplete: () -> Unit) {
        val createdDate = selectedDate.value ?: Date()
        val category = selectedCategory.value ?: defaultExpenseCategory

        if (content.isNullOrBlank() || amount.isNullOrBlank()) {
            notify.value = "Save failed!"
            return
        }

        executeTask(
            request = {
                val note = Note(
                    createdDate,
                    content,
                    amount.toDouble(),
                    category.id,
                    category,
                    fixedCostId
                )
                appRepository.addNote(note)
            },
            onSuccess = {
                notify.value = "Save successfully!"
                doOnComplete()
            },
            onError = {
                notify.value = "Save failed!"
            }
        )
    }

    private fun updateNote(content: String?, amount: String?, doOnComplete: () -> Unit) {
        val createdDate = selectedDate.value ?: Date()
        val category = selectedCategory.value ?: defaultExpenseCategory

        if (content.isNullOrBlank() || amount.isNullOrBlank()) {
            notify.value = "Save failed!"
            return
        }

        executeTask(
            request = {
                val note =
                    Note(
                        createdDate,
                        content,
                        amount.toDouble(),
                        category.id,
                        category,
                        fixedCostId
                    ).apply {
                        id = this@EditNoteViewModel.id
                    }

                appRepository.updateNote(note)
            },
            onSuccess = {
                notify.value = "Update successfully!"
                doOnComplete()
            },
            onError = {
                notify.value = "Update failed!"
            }
        )
    }

    fun deleteNote(doOnComplete: () -> Unit) {
        if (id == null) {
            notify.value = "Delete failed!"
            return
        }

        executeTask(
            request = {
                appRepository.deleteNote(id!!)
            },
            onSuccess = {
                notify.value = "Delete successfully!"
                doOnComplete()
            },
            onError = {
                notify.value = "Delete failed!"
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