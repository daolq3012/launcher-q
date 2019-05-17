package kr.co.seoft.laqnotepad.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.laqnotepad.R
import kr.co.seoft.laqnotepad.data.Note
import kr.co.seoft.laqnotepad.data.Repo
import kr.co.seoft.laqnotepad.ui.edit.EditActivity
import kr.co.seoft.laqnotepad.ui.read.ReadActivity
import kr.co.seoft.laqnotepad.util.*
import seoft.co.kr.manage_two_step.Command
import seoft.co.kr.manage_two_step.CommandRepo

class MainActivity : AppCompatActivity() {

    companion object {

        const val PKG_NAME = "kr.co.seoft.laqnotepad"
        const val CLS_NAME = "kr.co.seoft.laqnotepad.ui.main.MainActivity"
        const val NEW_NOTE = 1000
        const val EDIT_NOTE = 1100

        const val NONE_FOUCS_ITEM = -1
        const val FINDING = -2


        // for laq short cut connect
        const val LAQ_WRITE_MEMO = "LAQ_WRITE_MEMO"
        const val LAQ_FIND_NOTE = "LAQ_FIND_NOTE"
        const val LAQ_ = "LAQ_"
        const val LAQ_0 = "LAQ_0"
        const val LAQ_1 = "LAQ_1"
        const val LAQ_2 = "LAQ_2"
        const val LAQ_3 = "LAQ_3"

    }


    /**
     * FINIDNG -> after search
     * NONE_FOUCS_ITEM -> init status
     * else -> status is set note's id (int)
     */
    var status = NONE_FOUCS_ITEM

    lateinit var noteRVAdapter : NoteRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

        rvNote.layoutManager = LinearLayoutManager(this)
        noteRVAdapter = NoteRVAdapter(this){ note, isLongClick ->

            if(isLongClick) {
                if(status != FINDING) focusOn(note.id)
            } else {
                startActivityForResult(Intent(applicationContext,ReadActivity::class.java).apply {
                    putExtra(EK_ID,note.id)
                    putExtra(EK_CONTENT,note.content)
                }, EDIT_NOTE)
            }

        }
        rvNote.adapter = noteRVAdapter
        initListener()
        updateNotes()
    }

    override fun onResume() {
        super.onResume()
        adjustShortCutCmd()
        focusInit()
    }


    fun adjustShortCutCmd() {

        val getNormalMsg = intent.getStringExtra(Command.NORMAL_MESSAGE)

        when (getNormalMsg) {
            LAQ_WRITE_MEMO -> {
                val getEditMsg = intent.getStringExtra(Command.EDIT_MESSAGE)

                "LAQ_WRITE_MEMO $getEditMsg".i()

            }
            LAQ_FIND_NOTE -> {
                val getEditMsg = intent.getStringExtra(Command.EDIT_MESSAGE)
                "LAQ_FIND_NOTE $getEditMsg".i()

            }
            LAQ_0 -> {
                "LAQ_0".i()

            }
            LAQ_1 -> {
                "LAQ_1".i()

            }
            LAQ_2 -> {
                "LAQ_2".i()

            }
            LAQ_3 -> {
                "LAQ_3".i()

            }
        }

//        val getEditMsg = intent.getStringExtra(Command.EDIT_MESSAGE)
//
//        const val LAQ_WRITE_MEMO = "LAQ_WRITE_MEMO"
//        const val LAQ_FIND_NOTE = "LAQ_FIND_NOTE"
//        const val LAQ_ = "LAQ_"
//        const val LAQ_0 = "LAQ_0"
//        const val LAQ_1 = "LAQ_1"
//        const val LAQ_2 = "LAQ_2"
//        const val LAQ_3 = "LAQ_3"
    }

    private fun focusFinding() {
        status = FINDING
        tvShowAll.visibility = View.VISIBLE
        tvWrite.visibility = View.GONE
        tvSetting.visibility = View.GONE
        tvFind.visibility = View.GONE
        tvMoveTop.visibility = View.GONE
        tvMoveBottom.visibility = View.GONE
        tvQuickOff.visibility = View.GONE
        tvQuickOn.visibility = View.GONE
    }

    private fun focusInit() {
        status = NONE_FOUCS_ITEM
        tvShowAll.visibility = View.GONE
        tvWrite.visibility = View.VISIBLE
        tvSetting.visibility = View.VISIBLE
        tvFind.visibility = View.VISIBLE
        tvMoveTop.visibility = View.GONE
        tvMoveBottom.visibility = View.GONE
        tvQuickOff.visibility = View.GONE
        tvQuickOn.visibility = View.GONE
        updateNotes()
    }

    private fun focusOn(id:Int){
        status = id
        tvShowAll.visibility = View.GONE
        tvWrite.visibility = View.GONE
        tvSetting.visibility = View.GONE
        tvFind.visibility = View.GONE
        tvMoveTop.visibility = View.VISIBLE
        tvMoveBottom.visibility = View.VISIBLE

        tvQuickOff.visibility = View.VISIBLE
        tvQuickOn.visibility = View.VISIBLE

        if(Repo.getQuicks().contains(id)) tvQuickOn.visibility = View.GONE
        else tvQuickOff.visibility = View.GONE
        updateNotes()
    }

    fun updateNotes(){
        noteRVAdapter.updateNotes(Repo.getNotes())
        refreshTwoStep()
    }

    fun findNoteFromWord(word:String){
        val findNotes = Repo.getNotes()
            .filter { it.content.contains(word) }
            .toList().toMutableList()

        if(findNotes.isNotEmpty()) {
            noteRVAdapter.updateNotes(findNotes)
            focusFinding()
        }
        else "검색결과가 없습니다".toasti()
    }

    fun refreshTwoStep(){

        var insertingCmds = mutableListOf<Command>()

        val quickWriteCmd = Command(
            title = "빠른 노트 작성",
            pkgName = PKG_NAME,
            cls = CLS_NAME,
            normalMessage = LAQ_WRITE_MEMO,
            useEdit = true
        )

        val quickSearchCmd = Command(
            title = "빠른 노트 찾기",
            pkgName = PKG_NAME,
            cls = CLS_NAME,
            normalMessage = LAQ_FIND_NOTE,
            useEdit = true
        )

        val notes = Repo.getNotes()

        insertingCmds.add(quickWriteCmd)
        insertingCmds.add(quickSearchCmd)

        Repo.getQuicks().forEachIndexed { idx, quickId ->
            insertingCmds.add(getShortCutNoteCmd(notes,idx ,quickId))
        }

        CommandRepo.deleteFromPkgName(this,PKG_NAME)
        CommandRepo.insertCommands(this,insertingCmds)
    }

    fun getShortCutNoteCmd(notes: MutableList<Note>, idx:Int ,quickId: Int) : Command {

        val note = notes.filter { it.id == quickId }
        return Command(
            title = note[0].content.split("\n")[0],
            pkgName = PKG_NAME,
            cls = CLS_NAME,
            normalMessage = "${LAQ_}$idx",
            useEdit = false
        )

    }

    fun initListener(){

        tvWrite.setOnClickListener { _ ->
            startActivityForResult(Intent(applicationContext,EditActivity::class.java).apply {
                putExtra(EK_ID,EditActivity.NEW_NOTE)
            }, NEW_NOTE)
        }

        tvFind.setOnClickListener { _ ->
            AlertDialog.Builder(this).showDialogWithInput(
                message = "검색어 입력",
                postiveBtText = "확인",
                negativeBtText = "취소",
                cbPostive = {
                    if(it.isNotEmpty()) findNoteFromWord(it)
                },
                cbNegative = {

                },
                inputType = InputType.TYPE_CLASS_TEXT,
                text = ""
            )
        }

        tvShowAll.setOnClickListener { _ ->
            focusInit()
        }

        tvSetting.setOnClickListener { _ ->

        }

        tvMoveTop.setOnClickListener { _ ->
            if(status == NONE_FOUCS_ITEM) return@setOnClickListener

            var notes = Repo.getNotes()
            val idx = findIdxById(status,notes)
            val tmpNote = notes[idx]
            notes.removeAt(idx)
            notes.add(0,tmpNote)
            Repo.setNotes(notes)
            focusInit()
        }

        tvMoveBottom.setOnClickListener { _ ->
            if(status == NONE_FOUCS_ITEM) return@setOnClickListener

            var notes = Repo.getNotes()
            val idx = findIdxById(status,notes)
            val tmpNote = notes[idx]
            notes.removeAt(idx)
            notes.add(notes.size,tmpNote)
            Repo.setNotes(notes)
            focusInit()
        }

        tvQuickOn.setOnClickListener { _ ->
            if(status == NONE_FOUCS_ITEM) return@setOnClickListener
            if(!Repo.addQuicks(status)) "최대 4개까지의 바로가기만 등록할 수 있습니다".toasti()
            "바로가기 등록 완료".toasti()
            focusInit()
        }

        tvQuickOff.setOnClickListener { _ ->
            if(status == NONE_FOUCS_ITEM) return@setOnClickListener
            Repo.removeQuicks(status)
            "바로가기 해제 완료".toasti()
            focusInit()
        }

    }

    fun findIdxById(id:Int, notes:MutableList<Note>):Int{
        notes
            .forEachIndexed { index, note ->
            if(note.id == id){
                return index
            }
        }
        return -1
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data !=null) {
            when (requestCode) {
                NEW_NOTE -> {
                    val savingString = data.getStringExtra(EK_CONTENT)
                    Repo.addNotes(savingString)
                    updateNotes()
                }
                EDIT_NOTE ->{
                    val isRemove = data.getBooleanExtra(EK_IS_REMOVE,false)
                    if(isRemove) {
                        val removingId = data.getIntExtra(EK_ID,-1)
                        Repo.removeQuicks(removingId)
                        Repo.removeNoteById(removingId)
                    } else {
                        val savingString = data.getStringExtra(EK_CONTENT)
                        val savingId = data.getIntExtra(EK_ID,-1)
                        Repo.editNotes(savingId,savingString)
                    }
                    updateNotes()
                }
            }
        }
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    override fun onBackPressed() {
        if(status != NONE_FOUCS_ITEM) focusInit()
        else super.onBackPressed()
    }

}
