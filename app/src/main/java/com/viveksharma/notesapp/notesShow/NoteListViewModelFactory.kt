package com.viveksharma.notesapp.notesShow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viveksharma.notesapp.database.NoteDatabaseDao

class NoteListViewModelFactory(
    private val dao: NoteDatabaseDao
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}