package com.ndmq.moneynote.utils.constant

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun formatNumberWithDots(input: Double): String {
    val decimalFormat = DecimalFormat("#,###.##")

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    decimalFormat.decimalFormatSymbols = symbols

    return decimalFormat.format(input)
}
