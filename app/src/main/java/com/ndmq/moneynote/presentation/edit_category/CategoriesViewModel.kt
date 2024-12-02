package com.ndmq.moneynote.presentation.edit_category

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    /*
    * 1: Expense
    * 2: Income
    */
    val categoryType = MutableLiveData(1)

    val categories = MutableLiveData<List<Category>>(listOf())

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
}