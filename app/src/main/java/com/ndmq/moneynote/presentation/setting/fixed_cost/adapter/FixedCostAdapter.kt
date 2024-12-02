package com.ndmq.moneynote.presentation.setting.fixed_cost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.dto.Frequency
import com.ndmq.moneynote.databinding.ItemFixedCostBinding
import java.math.BigDecimal
import java.math.RoundingMode

class FixedCostAdapter(
    val frequencies: List<Frequency>
) : Adapter<FixedCostAdapter.FixedCostViewHolder>() {

    private val fixedCosts = mutableListOf<FixedCost>()

    private var onItemClick: (FixedCost) -> Unit = {}

    inner class FixedCostViewHolder(
        private val binding: ItemFixedCostBinding
    ) : ViewHolder(binding.root) {

        fun onBind(fixedCost: FixedCost) {
            binding.tvName.apply {
                text = fixedCost.title
                isSelected = true
            }

            val frequency = frequencies.find { it.type == fixedCost.frequency }?.content ?: ""
            val categoryAndFrequency = "${fixedCost.category.categoryName} / $frequency"
            binding.tvCategoryAndFrequency.text = categoryAndFrequency

            val amount = BigDecimal(fixedCost.amount).setScale(2, RoundingMode.HALF_UP).toString()
            binding.tvAmount.apply {
                val categoryType = fixedCost.category.categoryType
                text = "$amount$"
                setTextColor(
                    if (categoryType == 1)
                        ContextCompat.getColor(binding.tvName.context, R.color.errorColor)
                    else ContextCompat.getColor(binding.tvName.context, R.color.primaryColor)
                )
            }

            binding.ivImage.apply {
                setImageResource(fixedCost.category.iconResource)
                setColorFilter(fixedCost.category.tintColor)
            }
        }

        fun onClick(fixedCost: FixedCost) {
            binding.itemFixedCost.setOnClickListener {
                onItemClick(fixedCost)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixedCostViewHolder {
        val binding = ItemFixedCostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FixedCostViewHolder(binding)
    }

    override fun getItemCount(): Int = fixedCosts.size

    override fun onBindViewHolder(holder: FixedCostViewHolder, position: Int) {
        holder.onBind(fixedCosts[position])
        holder.onClick(fixedCosts[position])
    }

    fun setDataList(data: List<FixedCost>) {
        fixedCosts.clear()
        fixedCosts.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClick(func: (FixedCost) -> Unit) {
        onItemClick = func
    }
}