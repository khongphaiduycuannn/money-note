package com.ndmq.moneynote.data.repository

import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.Note
import com.ndmq.moneynote.data.source.local.AppDao
import com.ndmq.moneynote.presentation.setting.fixed_cost.FixedCostManager
import com.ndmq.moneynote.utils.getStartOfDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class AppRepository(
    private val appDao: AppDao
) {

    suspend fun getNotes() = getResult {
        appDao.getNotes()
    }

    suspend fun getNotesInMonth(startDate: Date, endDate: Date) = getResult {
        val result = appDao.getNotesInMonth(startDate, endDate).toMutableList()

        val fixedCosts = appDao.getFixedCosts()
        val fixedNotes = FixedCostManager.getNotesInMonth(fixedCosts, startDate, endDate)

        fixedNotes.forEach { fixedNote ->
            var check = true
            result.forEach {
                if (getStartOfDate(fixedNote.createdDate) == getStartOfDate(it.createdDate)
                    && fixedNote.fixedCostId == it.fixedCostId
                ) check = false
            }
            if (check) result += fixedNote
        }

        result
    }

    suspend fun addNote(note: Note) = getResult {
        appDao.addNote(note)
    }

    suspend fun addNotes(list: List<Note>) = getResult {
        appDao.addNotes(list)
    }

    suspend fun updateNote(note: Note) = getResult {
        appDao.updateNote(note)
    }

    suspend fun deleteNote(id: Long) = getResult {
        appDao.deleteNote(id)
    }

    suspend fun getFixedCosts() = getResult {
        appDao.getFixedCosts()
    }

    suspend fun addFixedCost(fixedCost: FixedCost) = getResult {
        appDao.addFixedCost(fixedCost)
    }

    suspend fun updateFixedCost(fixedCost: FixedCost) = getResult {
        appDao.updateFixedCost(fixedCost)
    }

    suspend fun deleteFixedCost(id: Long) = getResult {
        appDao.deleteFixedCost(id)
    }

    private suspend fun <T> getResult(
        request: suspend CoroutineScope.() -> T
    ): DataState<T> {
        return withContext(Dispatchers.IO) {
            try {
                DataState.Success(request())
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}