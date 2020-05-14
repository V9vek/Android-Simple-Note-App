package com.viveksharma.notesapp.notesShow

import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.viveksharma.notesapp.database.Note
import com.viveksharma.notesapp.database.NoteDatabaseDao
import kotlinx.coroutines.*

class NoteListViewModel(val dao: NoteDatabaseDao) : ViewModel() {


    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val notes = dao.getAllNotes()


    fun onClear() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    fun onUpdate(noteID: Long, checkBoxState: Boolean) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val note = dao.get(noteID)
                note.isChecked = checkBoxState
                Log.e("ViewModel", note.toString())
                dao.update(note)
            }
        }
    }

    fun onDelete(note: Note){
        uiScope.launch {
            withContext(Dispatchers.IO){
                dao.delete(note)
            }
        }
    }

    fun onUndo(note: Note){
        uiScope.launch {
            withContext(Dispatchers.IO){
                dao.insert(note)
            }
        }
    }

}