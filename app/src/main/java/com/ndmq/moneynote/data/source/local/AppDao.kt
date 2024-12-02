package com.ndmq.moneynote.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ndmq.moneynote.data.model.Category
import com.ndmq.moneynote.data.model.FixedCost
import com.ndmq.moneynote.data.model.Note
import java.util.Date

@Dao
interface AppDao {

    /* Note */
    @Query("select * from note order by createdDate desc")
    suspend fun getNotes(): List<Note>

    @Query("select * from note where createdDate >= :startDate AND createdDate < :endDate order by createdDate desc")
    suspend fun getNotesInMonth(startDate: Date, endDate: Date): List<Note>

    @Query("select note.* from note join category on note.categoryId = category.id where category.categoryName like '%' || :keyword || '%' order by createdDate desc")
    suspend fun getNotesByKeyword(keyword: String): List<Note>

    @Query("select note.* from note join category on note.categoryId = category.id where category.categoryName like '%' || :keyword || '%' and createdDate >= :startDate and createdDate < :endDate order by createdDate desc")
    suspend fun getNotesByKeyword(keyword: String, startDate: Date, endDate: Date): List<Note>

    @Insert
    suspend fun addNote(note: Note)

    @Transaction
    suspend fun addNotes(list: List<Note>) {
        list.forEach {
            addNote(it)
        }
    }

    @Update
    suspend fun updateNote(note: Note)

    @Query("delete from note where id = :id")
    suspend fun deleteNote(id: Long)

    @Query("select distinct createdDate from Note order by createdDate desc")
    suspend fun getAllYears(): List<Date>

    /* Fixed cost */
    @Query("select * from fixed_cost order by startDate desc")
    suspend fun getFixedCosts(): List<FixedCost>

    @Insert
    suspend fun addFixedCost(fixedCost: FixedCost)

    @Update
    suspend fun updateFixedCost(fixedCost: FixedCost)

    @Query("delete from fixed_cost where id = :id")
    suspend fun deleteFixedCost(id: Long)

    /* Category */
    @Query("select * from category")
    suspend fun getCategories(): List<Category>

    @Insert
    suspend fun addCategory(category: Category)

    @Transaction
    suspend fun addCategories(list: List<Category>) {
        list.forEach { addCategory(it) }
    }

    @Update
    suspend fun updateCategory(category: Category)

    @Query("delete from category where id = :id")
    suspend fun deleteCategory(id: Long)
}