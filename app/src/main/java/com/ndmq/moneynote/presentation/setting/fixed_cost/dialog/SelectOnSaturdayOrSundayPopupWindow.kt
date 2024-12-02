package com.ndmq.moneynote.presentation.setting.fixed_cost.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import com.ndmq.moneynote.databinding.PopupSelectOnSaturdayBinding

class SelectOnSaturdayOrSundayPopupWindow(
    val context: Context
) : PopupWindow() {

    private val binding by lazy { PopupSelectOnSaturdayBinding.inflate(LayoutInflater.from(context)) }

    private var onDoNothingButtonClick: () -> Unit = {}
    private var onBeforeButtonClick: () -> Unit = {}
    private var onAfterButtonClick: () -> Unit = {}

    init {
        contentView = binding.root
        height = WRAP_CONTENT
        width = WRAP_CONTENT
        isOutsideTouchable = true

        handleEvent()
    }

    private fun handleEvent() {
        binding.btnDoNothing.setOnClickListener {
            onDoNothingButtonClick()
        }

        binding.btnBefore.setOnClickListener {
            onBeforeButtonClick()
        }

        binding.btnAfter.setOnClickListener {
            onAfterButtonClick()
        }
    }

    fun setOnNothingButtonClick(func: () -> Unit) {
        onDoNothingButtonClick = func
    }

    fun setOnBeforeButtonClick(func: () -> Unit) {
        onBeforeButtonClick = func
    }

    fun setOnAfterButtonClick(func: () -> Unit) {
        onAfterButtonClick = func
    }
}