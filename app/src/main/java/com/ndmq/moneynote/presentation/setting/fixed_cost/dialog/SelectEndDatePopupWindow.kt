package com.ndmq.moneynote.presentation.setting.fixed_cost.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import com.ndmq.moneynote.databinding.PopupSelectEndDateBinding

class SelectEndDatePopupWindow(
    val context: Context
) : PopupWindow() {

    private val binding by lazy { PopupSelectEndDateBinding.inflate(LayoutInflater.from(context)) }

    private var onNoneButtonClick: () -> Unit = {}
    private var onSelectDateButtonClick: () -> Unit = {}

    init {
        contentView = binding.root
        height = WRAP_CONTENT
        width = WRAP_CONTENT
        isOutsideTouchable = true

        handleEvent()
    }

    private fun handleEvent() {
        binding.btnNone.setOnClickListener {
            onNoneButtonClick()
        }

        binding.btnAppointedDate.setOnClickListener {
            onSelectDateButtonClick()
        }
    }

    fun setOnNoneButtonClick(func: () -> Unit) {
        onNoneButtonClick = func
    }

    fun setOnSelectDateButtonClick(func: () -> Unit) {
        onSelectDateButtonClick = func
    }
}