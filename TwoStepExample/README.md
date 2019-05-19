# TwoStepExample

## Guide of connect to laq app
1. import module [manage_two_step] <br>(https://github.com/seose/launcher-q/tree/dev/TwoStepExample/manage_two_step)
2. add import command to real app's gradle [compile project(path: ':manage_two_step')] <br>( https://github.com/seose/launcher-q/blob/dev/TwoStepExample/app/build.gradle )
3. add/delete two step info (https://github.com/seose/launcher-q/blob/dev/TwoStepExample/app/src/main/java/seoft/co/kr/twostepexample/MainActivity.kt)

#### add
```
val insertCmd =  listOf(
  Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
  Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false),
  Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
  Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false),
  Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
  Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false)
  )

CommandRepo.insertCommands(this,insertCmd)
```

#### delete
```
CommandRepo.deleteFromPkgName(this,"seoft.co.kr.twostepexample")
```

4. get parameter from launcher's short cut ( https://github.com/seose/launcher-q/blob/dev/TwoStepExample/app/src/main/java/seoft/co/kr/twostepexample/AActivity.kt )
```
val getNormalMsg = intent.getStringExtra(Command.NORMAL_MESSAGE)
val getEditMsg = intent.getStringExtra(Command.EDIT_MESSAGE)

textView.text = "$getNormalMsg $getEditMsg"
Toast.makeText(this,"$getNormalMsg $getEditMsg",Toast.LENGTH_LONG).show()
```
