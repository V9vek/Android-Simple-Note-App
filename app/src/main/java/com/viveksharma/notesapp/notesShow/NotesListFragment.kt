package com.viveksharma.notesapp.notesShow

import android.media.MediaRouter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.viveksharma.notesapp.R
import com.viveksharma.notesapp.database.Note
import com.viveksharma.notesapp.database.NoteDatabase
import com.viveksharma.notesapp.databinding.FragmentNotesListBinding
import com.viveksharma.notesapp.notesShow.adapters.NotesRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_notes_list.*

/**
 * A simple [Fragment] subclass.
 */
class NotesListFragment : Fragment(), NotesRecyclerViewAdapter.OnNoteClickListener {

    private lateinit var binding: FragmentNotesListBinding
    private lateinit var noteListViewModel: NoteListViewModel
    private lateinit var adapter: NotesRecyclerViewAdapter


    override fun onNoteClick(note: Note) {
        this.findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToNoteAddEditFragment("edit", note.title, note.description, note.noteID))
    }

    override fun onCheckBoxClick(noteID: Long, checkBoxState: Boolean) {
        noteListViewModel.onUpdate(noteID, checkBoxState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_list, container, false)
        setHasOptionsMenu(true)

        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToNoteAddEditFragment("add", "", "", 0L))
        }

        setViewModel()

        setNotesListAdapter()

        swipeToDelete()

        return binding.root
    }


    private fun setViewModel() {
        val dao = NoteDatabase.getInstance(requireActivity().application).noteDatabaseDao
        val viewModelFactory = NoteListViewModelFactory(dao)
        noteListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteListViewModel::class.java)
    }

    private fun setNotesListAdapter() {
        adapter = NotesRecyclerViewAdapter(this)
        binding.recyclerView.adapter = adapter

        noteListViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun swipeToDelete() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val toBeDeletedNote = adapter.getNoteAt(viewHolder.adapterPosition)
                    noteListViewModel.onDelete(toBeDeletedNote)

                    //Undo SnackBar
                    Snackbar.make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", View.OnClickListener {
                            noteListViewModel.onUndo(toBeDeletedNote)
                        }).show()
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clearNotes -> clearAllNotes()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun clearAllNotes() {
        noteListViewModel.onClear()
        Snackbar.make(binding.recyclerView, "All Notes Cleared", Snackbar.LENGTH_SHORT).show()
    }

}
