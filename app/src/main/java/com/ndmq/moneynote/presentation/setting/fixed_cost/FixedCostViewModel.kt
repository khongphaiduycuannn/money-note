package com.ndmq.moneynote.presentation.setting.fixed_cost

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.FixedCost.Companion.DO_NO_THING
import com.ndmq.moneynote.data.model.FixedCost.Companion.NEVER
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.data.repository.DataState
import com.ndmq.moneynote.data.source.in_memory.defaultExpenseCategory
import com.ndmq.moneynote.data.source.in_memory.defaultFrequency
import com.ndmq.moneynote.presentation.setting.fixed_cost.FixedCostManager.mapFrequencyToDays
import com.ndmq.moneynote.utils.plusDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FixedCostViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val notify = MutableLiveData<String>(null)

    private var oldFixedCosts = listOf<FixedCost>()

    var id: Long? = null

    var categories = MutableLiveData<List<Category>>(listOf())

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val selectedCategory = MutableLiveData(defaultExpenseCategory)

    val startDate = MutableLiveData(Date())

    val endDate = MutableLiveData<Date>(null)

    val frequency = MutableLiveData(defaultFrequency)

    val onSaturdayOrSunday = MutableLiveData(DO_NO_THING)

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

    fun saveFixedCode(title: String?, amount: String?, doOnComplete: () -> Unit) {
        val startDate = startDate.value ?: Date()
        val endDate = this.endDate.value
        val category = selectedCategory.value ?: defaultExpenseCategory
        val frequency = frequency.value ?: defaultFrequency
        val onSaturdayAndSunday = onSaturdayOrSunday.value ?: DO_NO_THING

        if (title.isNullOrBlank() || amount.isNullOrBlank()) {
            notify.value = "Save failed!"
            return
        }

        val fixedCost = FixedCost(
            title,
            amount.toDouble(),
            category,
            frequency.type,
            startDate,
            endDate,
            onSaturdayAndSunday
        ).apply { id = this@FixedCostViewModel.id }

        executeTask(
            request = {
                val data = appRepository.getFixedCosts()
                if (data is DataState.Success) {
                    oldFixedCosts = data.data
                }

                if (id == null) {
                    appRepository.addFixedCost(fixedCost)
                } else {
                    appRepository.updateFixedCost(fixedCost)
                }
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

    fun deleteFixedCost(doOnComplete: () -> Unit) {
        id?.let {
            executeTask(
                request = {
                    appRepository.deleteFixedCost(it)
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
    }

    private fun getAppliedNotes(fixedCosts: List<FixedCost>): List<Note> {
        val notes = mutableListOf<Note>()
        fixedCosts.forEach { fixedCost ->
            if (fixedCost.id == null) return@forEach

            val startDate = fixedCost.startDate
            var endDate = fixedCost.endDate

            if (endDate == null) {
                endDate = Date().apply {
                    time = startDate.time
                    plusDay(2000)
                }
            }

            if (fixedCost.frequency == NEVER) {
                endDate = Date().apply { time = startDate.time }
            }

            while (startDate.time <= endDate.time) {
                val note = Note(
                    fixedCost.startDate,
                    fixedCost.title,
                    fixedCost.amount,
                    fixedCost.category.id,
                    fixedCost.category
                )
                notes.add(note)

                startDate.plusDay(mapFrequencyToDays(fixedCost.frequency))
            }
        }
        return notes
    }
}