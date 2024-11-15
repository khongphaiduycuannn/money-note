package com.ndmq.moneynote.presentation.setting.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.databinding.PopupSelectCategoryBinding
import com.ndmq.moneynote.presentation.setting.adapter.FixedCostCategoryAdapter

class SelectCategoryPopupWindow(
    val context: Context
) : PopupWindow() {

    private val binding by lazy { PopupSelectCategoryBinding.inflate(LayoutInflater.from(context)) }

    private val fixedCostCategoryAdapter = FixedCostCategoryAdapter()

    init {
        contentView = binding.root
        height = WRAP_CONTENT
        width = WRAP_CONTENT
        isOutsideTouchable = true
        initView()
    }

    private fun initView() {
        binding.rclCategories.layoutManager = LinearLayoutManager(context)
        binding.rclCategories.adapter = fixedCostCategoryAdapter
    }

    fun setDataList(data: List<Category>) {
        fixedCostCategoryAdapter.setCategoriesList(data)
    }

    fun setOnCategorySelected(func: (Category) -> Unit) {
        fixedCostCategoryAdapter.setOnCategorySelected {
            func(it)
        }
    }
}