package com.ndmq.moneynote.presentation.calendar

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer
import com.ndmq.moneynote.R

class DayViewContainer(
    view: View
) : ViewContainer(view) {

    val llDateItem: LinearLayout = view.findViewById(R.id.llDateItem)
    val tvDate: TextView = view.findViewById(R.id.tvDate)
    val tvIncome: TextView = view.findViewById(R.id.tvIncome)
    val tvExpense: TextView = view.findViewById(R.id.tvExpense)
}