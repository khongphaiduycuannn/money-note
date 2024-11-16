package com.ndmq.moneynote.data.source.in_memory

import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.Note

object InMemoryDataSource {

    private val notes = mutableListOf<Note>()

    private val fixedCosts = mutableListOf<FixedCost>()

    fun getNotes(): List<Note> = notes

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun updateNote(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
    }

    fun getFixedCosts(): List<FixedCost> = fixedCosts

    fun addFixedCost(fixedCost: FixedCost) {
        fixedCosts.add(fixedCost.apply { id = (1..100000).random().toLong() })
    }

    fun updateFixedCost(fixedCost: FixedCost) {
        fixedCosts.forEachIndexed { index, value ->
            if (value.id == fixedCost.id) {
                fixedCosts[index] = fixedCost
            }
        }
    }

    fun deleteFixedCost(id: Long) {
        fixedCosts.removeAll { it.id == id }
    }
}