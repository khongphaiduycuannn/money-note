package com.ndmq.moneynote.presentation.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.PeriodicMoney.Companion.DO_NO_THING
import com.ndmq.moneynote.data.model.dto.Frequency
import com.ndmq.moneynote.data.source.defaultExpenseCategory
import com.ndmq.moneynote.data.source.defaultFrequency
import java.util.Date

class FixedCostViewModel : ViewModel() {

    var categories = listOf<Category>()

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
}