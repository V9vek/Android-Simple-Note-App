package com.viveksharma.notesapp.notesShow.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.viveksharma.notesapp.R
import com.viveksharma.notesapp.database.Note

class NotesRecyclerViewAdapter(private var onNoteClickListener: OnNoteClickListener) :
    androidx.recyclerview.widget.ListAdapter<Note, NotesRecyclerViewAdapter.ViewHolder>(NoteDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onNoteClickListener, position)
    }

    fun getNoteAt(position: Int): Note{
        return getItem(position)
    }


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle = itemView.findViewById<TextView>(R.id.noteTitle)
        val noteDescription = itemView.findViewById<TextView>(R.id.noteDescription)
        val noteCheckBox = itemView.findViewById<CheckBox>(R.id.checkbox)

        fun bind(currentItem: Note, onNoteClickListener: OnNoteClickListener, position: Int) {
            noteTitle.text = currentItem.title
            noteDescription.text = currentItem.description
            noteCheckBox.isChecked = currentItem.isChecked

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClickListener.onNoteClick(currentItem)
                }
            }

            noteCheckBox.setOnClickListener {
                currentItem.isChecked = noteCheckBox.isChecked
                Log.e("Checkbox", currentItem.isChecked.toString())
                onNoteClickListener.onCheckBoxClick(currentItem.noteID, currentItem.isChecked)
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_note_list_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    //DiffUtil
    class NoteDiffUtilCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteID == newItem.noteID
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    interface OnNoteClickListener {
        fun onNoteClick(note: Note)
        fun onCheckBoxClick(noteID: Long, checkBoxState: Boolean)
    }


}