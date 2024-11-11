package com.ndmq.moneynote.data.source

import com.ndmq.moneynote.data.model.Note

object InMemoryDataSource {

    private val notes = mutableListOf<Note>()

    fun getNotes(): List<Note> = notes

    fun addNote(note: Note) {
        notes.add(note)
    }
}