package com.ndmq.moneynote.data.source.in_memory

import android.graphics.Color
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_2_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_2_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_3_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_3_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_4_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_5_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_DAY
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_HALF_YEAR
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_MONTH
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_WEEK
import com.ndmq.moneynote.data.model.FixedCost.Companion.EVERY_YEAR
import com.ndmq.moneynote.data.model.FixedCost.Companion.NEVER
import com.ndmq.moneynote.data.model.dto.Frequency

val defaultColorResources = listOf(
    R.color.color1,
    R.color.color2,
    R.color.color3,
    R.color.color4,
    R.color.color5,
    R.color.color6,
    R.color.color7,
    R.color.color8,
    R.color.color9,
    R.color.color10,
    R.color.color11,
    R.color.color12,
    R.color.color13,
    R.color.color14,
    R.color.color15,
    R.color.color16,
    R.color.color17,
    R.color.color18,
    R.color.color19,
    R.color.color20,
    R.color.color21,
    R.color.color22,
    R.color.color23,
    R.color.color24,
    R.color.color25,
    R.color.color26,
    R.color.color27,
    R.color.color28,
    R.color.color29,
    R.color.color30,
    R.color.color31,
    R.color.color32,
    R.color.color33,
    R.color.color34,
    R.color.color35,
    R.color.color36,
    R.color.color37,
    R.color.color38,
    R.color.color39,
    R.color.color40,
)

val defaultIconResources = listOf(
    R.drawable.ic_category_8,
    R.drawable.ic_category_9,
    R.drawable.ic_category_10,
    R.drawable.ic_category_11,
    R.drawable.ic_category_12,
    R.drawable.ic_category_13,
    R.drawable.ic_category_14,
    R.drawable.ic_category_15,
    R.drawable.ic_category_16,
    R.drawable.ic_category_17,
    R.drawable.ic_category_18,
    R.drawable.ic_category_19,
    R.drawable.ic_category_20,
    R.drawable.ic_category_21,
    R.drawable.ic_category_22,
    R.drawable.ic_category_23,
    R.drawable.ic_category_24,
    R.drawable.ic_category_25,
    R.drawable.ic_category_26,
    R.drawable.ic_category_27,
    R.drawable.ic_category_28,
    R.drawable.ic_category_29,
    R.drawable.ic_category_30,
    R.drawable.ic_category_31,
    R.drawable.ic_category_32,
    R.drawable.ic_category_33,
    R.drawable.ic_category_34,
    R.drawable.ic_category_35,
    R.drawable.ic_category_36,
    R.drawable.ic_category_37,
    R.drawable.ic_category_38,
    R.drawable.ic_category_39,
    R.drawable.ic_category_40,
    R.drawable.ic_category_41,
    R.drawable.ic_category_42,
    R.drawable.ic_category_43,
    R.drawable.ic_category_44,
    R.drawable.ic_category_45,
    R.drawable.ic_category_46,
    R.drawable.ic_category_47,
    R.drawable.ic_category_48,
    R.drawable.ic_category_49,
    R.drawable.ic_category_50
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

val frequencies = listOf(
    Frequency("Never", NEVER),
    Frequency("Every day", EVERY_DAY),
    Frequency("Every week", EVERY_WEEK),
    Frequency("Every 2 weeks", EVERY_2_WEEK),
    Frequency("Every 3 weeks", EVERY_3_WEEK),
    Frequency("Every month", EVERY_MONTH),
    Frequency("Every 2 months", EVERY_2_MONTH),
    Frequency("Every 3 months", EVERY_3_MONTH),
    Frequency("Every 4 months", EVERY_4_MONTH),
    Frequency("Every 5 months", EVERY_5_MONTH),
    Frequency("Half year", EVERY_HALF_YEAR),
    Frequency("Every year", EVERY_YEAR)
)

val defaultFrequency = Frequency("Never", NEVER)
