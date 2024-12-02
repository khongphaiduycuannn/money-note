package com.ndmq.moneynote.presentation.setting.fixed_cost

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FixedCostsListViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val fixedCosts = MutableLiveData<List<FixedCost>>(listOf())

    fun fetchData() {
        executeTask(
            request = {
                appRepository.getFixedCosts()
            },
            onSuccess = {
                fixedCosts.value = it
            }
        )
    }
}