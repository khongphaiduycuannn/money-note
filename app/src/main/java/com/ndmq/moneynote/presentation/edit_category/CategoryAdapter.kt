package com.ndmq.moneynote.presentation.edit_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.databinding.ItemEditCategoryBinding

class CategoryAdapter : Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories = mutableListOf<Category>()

    private var onCategoryClick: (Category) -> Unit = {}

    inner class CategoryViewHolder(
        private val binding: ItemEditCategoryBinding
    ) : ViewHolder(binding.root) {

        fun onBind(category: Category) {
            binding.ivIcon.setImageResource(category.iconResource)
            binding.ivIcon.setColorFilter(category.tintColor)

            binding.tvName.text = category.categoryName

            binding.itemCategory.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemEditCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(categories[position])
    }

    fun setDataList(data: List<Category>) {
        val prevSize = categories.size
        categories.clear()
        if (prevSize > 0) {
            notifyItemRangeRemoved(0, prevSize)
        }

        categories.addAll(data)
        if (data.isNotEmpty()) {
            notifyItemRangeInserted(0, data.size)
        }
    }

    fun setOnCategoryClick(func: (Category) -> Unit) {
        onCategoryClick = func
    }
}