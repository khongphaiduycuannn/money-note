package com.ndmq.moneynote.presentation.edit_category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.databinding.ItemEditCategoryColorBinding

class EditCategoryColorAdapter(
    var onColorClick: (Int) -> Unit = {}
) : Adapter<EditCategoryColorAdapter.EditCategoryColorViewHolder>() {

    private val colors = mutableListOf<Int>()

    private var selectedColor: Int? = null

    inner class EditCategoryColorViewHolder(
        private val binding: ItemEditCategoryColorBinding
    ) : ViewHolder(binding.root) {

        fun onBind(color: Int) {
            binding.llColor.setCardBackgroundColor(color)

            binding.ivOutline.setBackgroundResource(
                if (color == selectedColor) R.drawable.bg_item_edit_category_selected
                else R.drawable.bg_item_edit_category
            )

            binding.itemIcon.setOnClickListener {
                onColorClick(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditCategoryColorViewHolder {
        val binding = ItemEditCategoryColorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EditCategoryColorViewHolder(binding)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: EditCategoryColorViewHolder, position: Int) {
        holder.onBind(colors[position])
    }

    fun setDataList(data: List<Int>) {
        val prevSize = colors.size
        colors.clear()
        if (prevSize > 0) {
            notifyItemRangeRemoved(0, prevSize)
        }

        colors.addAll(data)
        if (data.isNotEmpty()) {
            notifyItemRangeInserted(0, data.size)
        }
    }

    fun setSelectedColor(color: Int) {
        val oldCategory = selectedColor
        selectedColor = color

        val oldPosition = colors.indexOf(oldCategory)
        val newPosition = colors.indexOf(selectedColor)
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition)
        }
        if (newPosition != -1) {
            notifyItemChanged(newPosition)
        }
    }
}