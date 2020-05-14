package com.viveksharma.notesapp.noteAddEdit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viveksharma.notesapp.database.Note
import com.viveksharma.notesapp.database.NoteDatabaseDao
import kotlinx.coroutines.*

class NoteAddEditViewModel(val dao: NoteDatabaseDao) : ViewModel() {

    private val _navigateToNoteList = MutableLiveData<Boolean>()
    val navigateToNoteList: LiveData<Boolean> get() = _navigateToNoteList

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun onAddNote(note: Note) {
        uiScope.launch {
            insert(note)
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            dao.insert(note)
        }
        _navigateToNoteList.value = true
    }

    fun doneNavigation() {
        _navigateToNoteList.value = null
    }

    fun onUpdateSelectedNote(noteID: Long, title: String, description: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val note = dao.get(noteID)
                note.title = title
                note.description = description
                dao.update(note)
            }
        }
        _navigateToNoteList.value = true
    }

}