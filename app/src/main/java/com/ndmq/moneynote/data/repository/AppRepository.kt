package com.ndmq.moneynote.data.repository

import com.ndmq.moneynote.data.model.Category
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

    /* Note */
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

    suspend fun getNotesByKeyword(keyword: String, start: Date? = null, end: Date? = null) =
        getResult {
            val result =
                if (start == null && end == null) {
                    appDao.getNotesByKeyword(keyword).toMutableList()
                } else {
                    appDao.getNotesByKeyword(keyword, start!!, end!!).toMutableList()
                }

            val fixedCosts =
                appDao.getFixedCosts().filter { it.category.categoryName.contains(keyword, true) }
            val fixedNotes = FixedCostManager.getNotesInMonth(fixedCosts, Date(0), Date())

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

    suspend fun getAllYear() = getResult {
        appDao.getAllYears()
    }

    /* Fixed cost */
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

    /* Category */
    suspend fun getCategories() = getResult {
        appDao.getCategories()
    }

    suspend fun addCategories(list: List<Category>) = getResult {
        appDao.addCategories(list)
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