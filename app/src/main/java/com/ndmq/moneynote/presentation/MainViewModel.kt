package com.ndmq.moneynote.presentation

import androidx.lifecycle.MutableLiveData
import com.ndmq.moneynote.base.BaseViewModel
import com.ndmq.moneynote.data.repository.AppRepository
import com.ndmq.moneynote.data.repository.MMKVRepository
import com.ndmq.moneynote.data.source.in_memory.categories
import com.ndmq.moneynote.utils.constant.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val mmkvRepository: MMKVRepository,
) : BaseViewModel() {

    val currentScreen = MutableLiveData(Screen.ADD_NOTE)

    fun initDefaultCategories() {
        if (!mmkvRepository.isFirstTime()) {
            executeTask(
                request = {
                    appRepository.addCategories(categories)
                },
                onSuccess = {

                }
            )
        }
    }
}