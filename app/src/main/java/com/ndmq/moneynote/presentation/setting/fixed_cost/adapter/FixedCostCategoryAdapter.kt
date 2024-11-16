package com.ndmq.moneynote.presentation.setting.fixed_cost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.databinding.ItemFixedCostCategoryBinding

class FixedCostCategoryAdapter : Adapter<FixedCostCategoryAdapter.FixedCostCategoryViewHolder>() {

    private val categories = mutableListOf<Category>()

    private var onCategorySelected: (Category) -> Unit = {}

    inner class FixedCostCategoryViewHolder(
        private val binding: ItemFixedCostCategoryBinding
    ) : ViewHolder(binding.root) {

        fun onBind(category: Category) {
            binding.ivCategoryIcon.apply {
                setImageResource(category.iconResource)
                setColorFilter(category.tintColor)
            }
            binding.tvCategoryName.text = category.categoryName
        }

        fun onClick(category: Category) {
            binding.itemCategory.setOnClickListener {
                onCategorySelected(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixedCostCategoryViewHolder {
        val binding = ItemFixedCostCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FixedCostCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: FixedCostCategoryViewHolder, position: Int) {
        holder.onBind(categories[position])
        holder.onClick(categories[position])
    }

    fun setCategoriesList(data: List<Category>) {
        categories.clear()
        categories.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnCategorySelected(func: (Category) -> Unit) {
        onCategorySelected = func
    }
}