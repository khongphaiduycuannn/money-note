package com.ndmq.moneynote.presentation.add_note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.databinding.ItemCategoryAddNoteBinding
import com.ndmq.moneynote.databinding.ItemCategoryEditAddNoteBinding

class CategoryAdapter(
    var onCategoryClick: (Category?) -> Unit = {}
) : Adapter<ViewHolder>() {

    private val categories = mutableListOf<Category>()

    private var selectedCategory: Category? = null

    inner class CategoryViewHolder(private val binding: ItemCategoryAddNoteBinding) :
        ViewHolder(binding.root) {

        fun onBind(category: Category) {
            with(binding) {
                btnCategory.setBackgroundResource(
                    if (category == selectedCategory)
                        R.drawable.bg_item_category_add_note_selected
                    else R.drawable.bg_item_category_add_note
                )
                ivCategoryIcon.setImageResource(category.iconResource)
                ivCategoryIcon.setColorFilter(category.tintColor)
                tvCategoryName.text = category.categoryName
                tvCategoryName.isSelected = true
            }
        }

        fun onClick(category: Category) {
            binding.btnCategory.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    inner class EditViewHolder(private val binding: ItemCategoryEditAddNoteBinding) :
        ViewHolder(binding.root) {

        fun onClick() {
            binding.itemEditCategory.setOnClickListener {
                onCategoryClick(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CATEGORY_TYPE -> {
                val binding = ItemCategoryAddNoteBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                CategoryViewHolder(binding)
            }

            else -> {
                val binding = ItemCategoryEditAddNoteBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                EditViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = categories.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.onBind(categories[position])
                holder.onClick(categories[position])
            }

            is EditViewHolder -> {
                holder.onClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == categories.size) EDIT_CATEGORY_TYPE
        else CATEGORY_TYPE
    }

    fun setCategoryList(data: List<Category>) {
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

    fun setSelectedCategory(category: Category) {
        val oldCategory = selectedCategory?.copy()
        selectedCategory = category

        val oldPosition = categories.indexOf(oldCategory)
        val newPosition = categories.indexOf(selectedCategory)
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition)
        }
        if (newPosition != -1) {
            notifyItemChanged(newPosition)
        }
    }

    companion object {

        const val CATEGORY_TYPE = 1
        const val EDIT_CATEGORY_TYPE = 2
    }
}