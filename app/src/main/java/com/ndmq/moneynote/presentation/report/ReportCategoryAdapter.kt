package com.ndmq.moneynote.presentation.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.model.dto.CategoryNotes
import com.ndmq.moneynote.data.model.dto.getPercent
import com.ndmq.moneynote.data.model.dto.toListCategoryNotes
import com.ndmq.moneynote.databinding.ItemReportCategoryBinding
import java.math.BigDecimal
import java.math.RoundingMode

class ReportCategoryAdapter : Adapter<ViewHolder>() {

    private val categoriesNotes = mutableListOf<CategoryNotes>()

    inner class ReportCategoryViewHolder(private val binding: ItemReportCategoryBinding) :
        ViewHolder(binding.root) {

        fun onBind(categoryNotes: CategoryNotes) {
            val category = categoryNotes.category
            with(binding) {
                val roundedPercent = BigDecimal(categoriesNotes.getPercent(categoryNotes)).setScale(
                    2,
                    RoundingMode.HALF_UP
                )
                ivIcon.setImageResource(category.iconResource)
                ivIcon.setColorFilter(category.tintColor)
                tvName.text = category.categoryName
                tvAmount.text = "${categoryNotes.total}$"
                tvPercent.text = "$roundedPercent%"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReportCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categoriesNotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ReportCategoryViewHolder).onBind(categoriesNotes[position])
    }

    fun setDataList(data: List<Note>) {
        val oldSize = categoriesNotes.size
        categoriesNotes.clear()
        if (oldSize > 0) notifyItemRangeRemoved(0, oldSize)

        categoriesNotes.addAll(data.toListCategoryNotes())
        val newSize = categoriesNotes.size
        if (newSize > 0) notifyItemRangeInserted(0, newSize)
    }
}