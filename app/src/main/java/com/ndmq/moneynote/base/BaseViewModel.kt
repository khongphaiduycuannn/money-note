package com.ndmq.moneynote.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndmq.moneynote.data.repository.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected fun <T> executeTask(
        request: suspend CoroutineScope.() -> DataState<T>,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val response = request(this)) {
                is DataState.Success -> {
                    onSuccess(response.data)
                }

                is DataState.Error -> {
                    onError(response.exception)
                }
            }
        }
    }
}