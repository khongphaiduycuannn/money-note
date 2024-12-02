package com.ndmq.moneynote.presentation.edit_category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.databinding.ItemEditCategoryIconBinding

class EditCategoryIconAdapter(
    var onItemClick: (Int) -> Unit = {}
) :
    RecyclerView.Adapter<EditCategoryIconAdapter.EditCategoryIconViewHolder>() {

    private val iconResources = mutableListOf<Int>()

    private var selectedIcon: Int? = null

    private var selectedColor: Int? = null

    inner class EditCategoryIconViewHolder(
        private val binding: ItemEditCategoryIconBinding
    ) : ViewHolder(binding.root) {

        fun onBind(iconResource: Int) {
            binding.itemIcon.setBackgroundResource(
                if (selectedIcon == iconResource) R.drawable.bg_item_edit_category_selected
                else R.drawable.bg_item_edit_category
            )

            binding.ivIcon.setImageResource(iconResource)

            binding.ivIcon.setColorFilter(
                if (iconResource == selectedIcon) {
                    selectedColor ?: ContextCompat.getColor(
                        binding.ivIcon.context,
                        R.color.defaultIconColor
                    )
                } else {
                    ContextCompat.getColor(binding.ivIcon.context, R.color.defaultIconColor)
                }
            )

            binding.itemIcon.setOnClickListener {
                onItemClick(iconResource)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditCategoryIconViewHolder {
        val binding = ItemEditCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EditCategoryIconViewHolder(binding)
    }

    override fun getItemCount(): Int = iconResources.size

    override fun onBindViewHolder(holder: EditCategoryIconViewHolder, position: Int) {
        holder.onBind(iconResources[position])
    }

    fun setDataList(data: List<Int>) {
        val prevSize = iconResources.size
        iconResources.clear()
        if (prevSize > 0) {
            notifyItemRangeRemoved(0, prevSize)
        }

        iconResources.addAll(data)
        if (data.isNotEmpty()) {
            notifyItemRangeInserted(0, data.size)
        }
    }

    fun setSelectedIcon(iconResource: Int) {
        val oldResource = selectedIcon
        selectedIcon = iconResource

        val oldPosition = iconResources.indexOf(oldResource)
        val newPosition = iconResources.indexOf(selectedIcon)
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition)
        }
        if (newPosition != -1) {
            notifyItemChanged(newPosition)
        }
    }

    fun setSelectedColor(color: Int) {
        selectedColor = color
        val position = iconResources.indexOf(selectedIcon)
        notifyItemChanged(position)
    }
}