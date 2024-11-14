package com.ndmq.moneynote.presentation.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ndmq.moneynote.R
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.model.dto.DateNotes
import com.ndmq.moneynote.databinding.ItemCategoryDateDetailBinding
import com.ndmq.moneynote.databinding.ItemCategoryDateDetailHeaderBinding
import com.ndmq.moneynote.utils.dateMonthFormattedDate

class DetailDateNotesAdapter : Adapter<ViewHolder>() {

    private val dateNotesList = mutableListOf<DateNotes>()
    private val notes = mutableListOf<Any>()

    private var onNoteClick: (Note) -> Unit = { }

    inner class HeaderViewHolder(private val binding: ItemCategoryDateDetailHeaderBinding) :
        ViewHolder(binding.root) {

        fun onBind(dateNotes: DateNotes) {
            binding.tvDate.text = dateMonthFormattedDate(dateNotes.date)
            binding.tvTotal.text = "${dateNotes.total} $"
        }
    }

    inner class DetailViewHolder(private val binding: ItemCategoryDateDetailBinding) :
        ViewHolder(binding.root) {

        fun onBind(note: Note) {
            with(binding) {
                ivIcon.apply {
                    setImageResource(note.category.iconResource)
                    setColorFilter(note.category.tintColor)
                }
                
                tvName.text = note.category.categoryName
                tvName.isSelected = true

                tvNote.text = note.note
                tvNote.isSelected = true

                tvAmount.text = "${note.expense} $"
                tvAmount.setTextColor(
                    if (note.category.categoryType == 1) {
                        getColor(tvAmount.context, R.color.disableTextColor)
                    } else {
                        getColor(tvAmount.context, R.color.primaryColor)
                    }
                )
            }
        }

        fun onClick(note: Note) {
            binding.llItemCalendarDateDetail.setOnClickListener {
                onNoteClick(note)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (notes[position] is DateNotes) HEADER_TYPE else NOTE_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NOTE_TYPE -> {
                val binding = ItemCategoryDateDetailBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                DetailViewHolder(binding)
            }

            else -> {
                val binding = ItemCategoryDateDetailHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val dateNotes = notes[position]
                if (dateNotes is DateNotes) {
                    holder.onBind(dateNotes)
                }
            }

            is DetailViewHolder -> {
                val note = notes[position]
                if (note is Note) {
                    holder.onBind(note)
                    holder.onClick(note)
                }
            }
        }
    }

    fun setDateNotesList(data: List<DateNotes>) {
        dateNotesList.clear()
        notes.clear()

        data.forEach {
            dateNotesList.add(it)
            notes.add(it)
            notes.addAll(it.notes)
        }
        notifyDataSetChanged()
    }

    fun setOnNoteClick(func: (Note) -> Unit) {
        onNoteClick = func
    }

    companion object {

        const val HEADER_TYPE = 1
        const val NOTE_TYPE = 2
    }
}