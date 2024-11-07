package com.ndmq.moneynote.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ndmq.moneynote.utils.constant.Screen

class MainViewModel : ViewModel() {

    val currentScreen = MutableLiveData(Screen.ADD_NOTE)
}