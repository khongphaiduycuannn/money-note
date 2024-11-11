package com.ndmq.moneynote.utils.extension

import android.app.Activity
import androidx.core.content.ContextCompat

fun Activity.getColorFromResource(id: Int): Int {
    return ContextCompat.getColor(this, id)
}