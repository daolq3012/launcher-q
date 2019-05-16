package kr.co.seoft.laqnotepad.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*
import kr.co.seoft.laqnotepad.R
import kr.co.seoft.laqnotepad.data.Note
import kr.co.seoft.laqnotepad.data.Repo
import kr.co.seoft.laqnotepad.util.toCalendar
import kr.co.seoft.laqnotepad.util.toYYYYMMDD

class NoteRVAdapter(val context: Context, var cb:(note: Note, isLongClick : Boolean)->Unit)
    : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    var notes: MutableList<Note> = mutableListOf()

    lateinit var quicks : MutableList<Int>

    fun updateNotes(_notes:MutableList<Note>){
        notes = _notes
        quicks = Repo.getQuicks()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note,parent,false))
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val curNote = notes[position]

        val isStar = quicks.contains(curNote.id)

        val splittedString = curNote.content.split("\n")
        holder.tvContent.text = if(splittedString.size == 1) splittedString[0] else splittedString[1]
        holder.tvTitle.text = splittedString[0]
        holder.tvDate.text = "${curNote.date.toCalendar().toYYYYMMDD()} ${if(isStar)"★" else ""}"
        holder.itemView.setOnClickListener { _ ->
            cb.invoke(curNote,false)
        }

        holder.itemView.setOnLongClickListener { _ ->
            cb.invoke(curNote,true)
            return@setOnLongClickListener true
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvContent = view.tvContent
        var tvTitle = view.tvTitle
        var tvDate = view.tvDate
    }
}