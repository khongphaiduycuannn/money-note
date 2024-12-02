package com.ndmq.moneynote.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.databinding.ItemSearchTimeBinding

class SearchTimeAdapter : Adapter<SearchTimeAdapter.SearchTimeViewHolder>() {

    private var onTimeSelected: (year: Int?) -> Unit = {}

    inner class SearchTimeViewHolder(
        private val binding: ItemSearchTimeBinding
    ) : ViewHolder(binding.root) {

        fun onBind(year: String?) {
            binding.tvTime.text =
                year ?: binding.tvTime.context.getString(R.string.all_period)

            binding.tvTime.setOnClickListener {
                onTimeSelected(year?.toInt())
            }
        }
    }

    private val time = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTimeViewHolder {
        val binding = ItemSearchTimeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchTimeViewHolder(binding)
    }

    override fun getItemCount(): Int = time.size + 1

    override fun onBindViewHolder(holder: SearchTimeViewHolder, position: Int) {
        holder.onBind(
            if (position == 0) null
            else time[position - 1]
        )
    }

    fun setDataList(list: List<String>) {
        time.clear()
        time.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnTimeSelected(func: (year: Int?) -> Unit) {
        onTimeSelected = func
    }
}
