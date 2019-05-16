package kr.co.seoft.laqnotepad.data

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kr.co.seoft.laqnotepad.util.App
import java.util.*

object Repo {
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)

    private val SP_NOTES = "SP_NOTES"
    private val SP_QUICKS = "SP_QUICKS"

    fun getQuicks() : MutableList<Int> {
        val quicksSet = mPrefs.getStringSet(SP_QUICKS,null)
        if(quicksSet == null) return mutableListOf()
        return quicksSet.map { it.toInt() }.toMutableList()
    }

    fun setQuicks(quicks: MutableList<Int>){
        mPrefs.edit().putStringSet(SP_QUICKS,quicks.map { it.toString() }.toList().toSet()).apply()
    }

    fun removeQuicks(id:Int){
        val quicks = getQuicks()
        quicks.removeIf { it == id }
        setQuicks(quicks)
    }

    fun addQuicks(id:Int):Boolean{
        val quicks = getQuicks()
        if(quicks.size >= 4) return false
        quicks.add(id)
        setQuicks(quicks)
        return true
    }

    fun getNotes() : MutableList<Note> {
        val notesJson = mPrefs.getString(SP_NOTES,null)

        if(notesJson == null) return mutableListOf()

        val notesType = object : TypeToken<MutableList<Note>>() {}.type
        val notes = Gson().fromJson<MutableList<Note>>(notesJson, notesType)
        return notes
    }

    fun setNotes(notes : MutableList<Note>) {
        val notesJson = Gson().toJson(notes)
        mPrefs.edit().putString(SP_NOTES,notesJson).apply()
    }

    fun removeNoteById(id:Int) {
        val notes = getNotes()
        notes.removeIf { it.id == id }
        setNotes(notes)
    }

    fun addNotes(content : String) {
        val notes = getNotes()

        notes.add(Note(
            if(notes.size == 0) 0
            else notes.maxBy { it.id }!!.id + 1,
            content,
            Date()
        ))
        setNotes(notes)
    }


    fun editNotes(id:Int, content : String) {
        val notes = getNotes()
        notes.forEachIndexed { index, note ->
            if(id == note.id) {
                notes[index].content = content
                notes[index].date = Date()
            }
        }

        setNotes(notes)
    }


}