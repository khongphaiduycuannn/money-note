package com.ndmq.moneynote.utils

import android.graphics.Color
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Category

val categoryIcons = listOf(
    R.drawable.ic_hehe
)

val categoryTints = listOf(
    Color.CYAN
)

val categories = listOf(
    Category(R.drawable.ic_hehe, Color.CYAN, "Đám cưới a C", 1),
    Category(R.drawable.ic_hehe, Color.LTGRAY, "Đám cưới a D", 1),
    Category(R.drawable.ic_hehe, Color.MAGENTA, "Đám cưới a Đ", 2),
    Category(R.drawable.ic_hehe, Color.GREEN, "Đám cưới a K", 2)
)

val defaultExpenseCategory = categories[0]
val defaultIncomeCategory = categories[2]
