package com.viveksharma.notesapp.noteAddEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viveksharma.notesapp.database.NoteDatabaseDao

class NoteAddEditViewModelFactory(
    private val dao: NoteDatabaseDao
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteAddEditViewModel::class.java)) {
            return NoteAddEditViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}