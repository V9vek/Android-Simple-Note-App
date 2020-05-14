package com.viveksharma.notesapp.noteAddEdit

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.viveksharma.notesapp.R
import com.viveksharma.notesapp.database.Note
import com.viveksharma.notesapp.database.NoteDatabase
import com.viveksharma.notesapp.database.NoteDatabaseDao
import com.viveksharma.notesapp.databinding.FragmentNoteAddEditBinding
import com.viveksharma.notesapp.notesShow.NotesListFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class NoteAddEditFragment : Fragment() {

    private lateinit var binding: FragmentNoteAddEditBinding
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var noteAddEditViewModel: NoteAddEditViewModel
    //0 = Add Mode , 1 = Edit Mode
    private var mode: Int = 0
    private var noteID: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_add_edit, container, false)
        setHasOptionsMenu(true)

        //args
        val args = arguments?.let { NoteAddEditFragmentArgs.fromBundle(it) }

        //ViewModel
        val dao = NoteDatabase.getInstance(requireActivity().application).noteDatabaseDao
        val viewModelFactory = NoteAddEditViewModelFactory(dao)
        noteAddEditViewModel = ViewModelProvider(this, viewModelFactory).get(NoteAddEditViewModel::class.java)

        if(args?.mode.equals("edit")){
            //Edit Mode
            mode = 1
            binding.etTitle.setText(args?.title)
            binding.etDescription.setText(args?.description)
            noteID = args?.noteID!!
        }
        else{
            //Add Mode
        }


        noteAddEditViewModel.navigateToNoteList.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(NoteAddEditFragmentDirections.actionNoteAddEditFragmentToNotesListFragment())
                noteAddEditViewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.saveNote -> saveNote()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun saveNote() {

        title = binding.etTitle.text.toString()
        description = binding.etDescription.text.toString()

        val note = Note(title = title, description = description)

        if (mode == 0) {
            noteAddEditViewModel.onAddNote(note)
            Toast.makeText(activity, "Note Added", Toast.LENGTH_SHORT).show()
        }

        if (mode == 1){
            noteAddEditViewModel.onUpdateSelectedNote(noteID, title, description)
            Toast.makeText(activity, "Note Updated", Toast.LENGTH_SHORT).show()
        }

        //hiding soft keyboard
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
