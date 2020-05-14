package com.viveksharma.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDatabaseDao{
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM notes_table")
    fun deleteAll()

    @Query("SELECT * FROM notes_table ORDER BY isChecked ASC, noteID DESC")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE noteID = :key")
    fun get(key: Long): Note
}