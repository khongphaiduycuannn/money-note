package com.ndmq.moneynote.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formattedDate(date: Date): String {
    val format = SimpleDateFormat("dd.MM yyyy (EEE)", Locale.getDefault())
    return format.format(date)
}