package com.ndmq.moneynote.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ndmq.moneynote.utils.constant.Screen

fun Fragment.navigateTo(desScreen: Screen, id: Int, doOnNavigated: (Screen) -> Unit = {}) {
    try {
        findNavController().navigate(id)
        doOnNavigated(desScreen)
    } catch (_: Exception) {
    }
}