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
    Category(R.drawable.ic_water_bottle_1, Color.rgb(0, 167, 61), "Houseware", 1),
    Category(R.drawable.ic_dress_1, Color.rgb(20, 62, 156), "Clothes", 1),
    Category(R.drawable.ic_lipstick_1, Color.rgb(241, 81, 163), "Cosmetic", 1),
    Category(R.drawable.ic_wine_glass_1, Color.rgb(223, 207, 77), "Exchange", 1),
    Category(R.drawable.ic_fork_1, Color.rgb(227, 120, 12), "Food", 1),
    Category(R.drawable.ic_post_it_1, Color.rgb(227, 78, 79), "Education", 1),
    Category(R.drawable.ic_waterdrop_1, Color.rgb(35, 192, 236), "Electric bill", 1),
    Category(R.drawable.ic_train_cargo_1, Color.rgb(158, 97, 63), "Transport", 1),
    Category(R.drawable.ic_iphone_1, Color.rgb(78, 85, 84), "Contact", 1),
    Category(R.drawable.ic_wallet__1__1, Color.rgb(0, 166, 63), "Salary", 2),
    Category(R.drawable.ic_piggy_bank_1, Color.rgb(216, 120, 10), "Pocket money", 2),
    Category(R.drawable.ic_gift_1, Color.rgb(215, 83, 85), "Bonus", 2),
    Category(R.drawable.ic_purse_1, Color.rgb(43, 194, 233), "Side job", 2),
    Category(R.drawable.ic_money_bag_1, Color.rgb(70, 185, 172), "Investment", 2),
    Category(R.drawable.ic_save_money_1, Color.rgb(205, 135, 177), "Extra", 2)
)

val defaultExpenseCategory = categories[0]
val defaultIncomeCategory = categories[2]
