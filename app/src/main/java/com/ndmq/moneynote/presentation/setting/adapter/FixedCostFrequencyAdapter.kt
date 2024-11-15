package com.ndmq.moneynote.presentation.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.data.model.dto.Frequency
import com.ndmq.moneynote.databinding.ItemFixedCostFrequencyBinding

class FixedCostFrequencyAdapter :
    Adapter<FixedCostFrequencyAdapter.FixedCostFrequencyViewHolder>() {

    private val frequencies = mutableListOf<Frequency>()

    private var onFrequencySelected: (Frequency) -> Unit = {}

    inner class FixedCostFrequencyViewHolder(
        private val binding: ItemFixedCostFrequencyBinding
    ) : ViewHolder(binding.root) {

        fun onBind(frequency: Frequency) {
            binding.tvFrequency.text = frequency.content
        }

        fun onClick(frequency: Frequency) {
            binding.itemFrequency.setOnClickListener {
                onFrequencySelected(frequency)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FixedCostFrequencyViewHolder {
        val binding = ItemFixedCostFrequencyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FixedCostFrequencyViewHolder(binding)
    }

    override fun getItemCount(): Int = frequencies.size

    override fun onBindViewHolder(holder: FixedCostFrequencyViewHolder, position: Int) {
        holder.onBind(frequencies[position])
        holder.onClick(frequencies[position])
    }

    fun setDataList(data: List<Frequency>) {
        frequencies.clear()
        frequencies.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnFrequencySelected(func: (Frequency) -> Unit) {
        onFrequencySelected = func
    }
}