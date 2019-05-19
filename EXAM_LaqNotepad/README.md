# EXAM_LaqNotePad

## Introduce 
This project is example that connect to laq launcher and adjust short cut function

if connect with Laq Launcher App

1. launch specific packageName, clsName with params from Laq Launcher App
2. also, It's possilbe launch with string that typed edittext in Launcher

```
In the past, [Open app] -> [Execute function]
if connect Launcher app -> [open app with function]
save up to 1 step from 2 steps
```

so this app's fucntion ( refrence screen shot )

```
write note from launcher
find note from launcher
open specific note from launcher ( if regist preivous )
```

## Connect source 
https://github.com/seose/launcher-q/blob/dev/EXAM_LaqNotepad/app/src/main/java/kr/co/seoft/laqnotepad/ui/main/MainActivity.kt

```
    fun refreshTwoStep(){

        var insertingCmds = mutableListOf<Command>()

        val quickWriteCmd = Command(
            title = R.string.quick_write.TO_STRING(),
            pkgName = PKG_NAME,
            cls = CLS_NAME,
            normalMessage = LAQ_WRITE_MEMO,
            useEdit = true
        )

        val quickSearchCmd = Command(
            title = R.string.quick_note.TO_STRING(),
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
```

## Using source 
https://github.com/seose/launcher-q/blob/dev/EXAM_LaqNotepad/app/src/main/java/kr/co/seoft/laqnotepad/ui/main/MainActivity.kt
```
  fun adjustShortCutCmd(_newIntent:Intent? = null) {
        var newIntent : Intent
        if(_newIntent== null) newIntent = intent
        else newIntent = _newIntent

        val getNormalMsg = newIntent.getStringExtra(Command.NORMAL_MESSAGE)
            when (getNormalMsg) {
                LAQ_WRITE_MEMO -> {
                    val getEditMsg = newIntent.getStringExtra(Command.EDIT_MESSAGE)
                    "LAQ_WRITE_MEMO $getEditMsg".i()
                    writeNewNote(getEditMsg)
                }
                LAQ_FIND_NOTE -> {
                    val getEditMsg = newIntent.getStringExtra(Command.EDIT_MESSAGE)
                    "LAQ_FIND_NOTE $getEditMsg".i()
                    findNoteFromWord(getEditMsg)
                }
                LAQ_0 -> openNoteFromLaQ(0)
                LAQ_1 -> openNoteFromLaQ(1)
                LAQ_2 -> openNoteFromLaQ(2)
                LAQ_3 -> openNoteFromLaQ(3)
            }
    }
```
