package com.ndmq.moneynote.presentation.setting.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.data.model.dto.Frequency
import com.ndmq.moneynote.databinding.PopupSelectFrequencyBinding
import com.ndmq.moneynote.presentation.setting.adapter.FixedCostFrequencyAdapter

class SelectFrequencyPopupWindow(
    val context: Context
) : PopupWindow() {

    private val binding by lazy { PopupSelectFrequencyBinding.inflate(LayoutInflater.from(context)) }

    private val fixedCostFrequencyAdapter = FixedCostFrequencyAdapter()

    init {
        contentView = binding.root
        height = WRAP_CONTENT
        width = WRAP_CONTENT
        isOutsideTouchable = true
        initView()
    }

    private fun initView() {
        binding.rclFrequencies.layoutManager = LinearLayoutManager(context)
        binding.rclFrequencies.adapter = fixedCostFrequencyAdapter
    }

    fun setDataList(data: List<Frequency>) {
        fixedCostFrequencyAdapter.setDataList(data)
    }

    fun setOnFrequencySelected(func: (Frequency) -> Unit) {
        fixedCostFrequencyAdapter.setOnFrequencySelected {
            func(it)
        }
    }
}