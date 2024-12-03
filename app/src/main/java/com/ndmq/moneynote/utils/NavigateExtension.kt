package com.ndmq.moneynote.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ndmq.moneynote.utils.constant.Screen

fun Fragment.navigateTo(
    desScreen: Screen,
    id: Int,
    bundle: Bundle? = null,
    doOnNavigated: (Screen) -> Unit = {}
) {
    try {
        findNavController().navigate(id, bundle)
        doOnNavigated(desScreen)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}