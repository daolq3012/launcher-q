package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.data.model.toSaveString
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.toast
import seoft.co.kr.launcherq.utill.value


class ArrangeViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "ArrangeViewModel#$#"

    companion object {
        val NONE_PICK = "NONE_PICK"
    }

    // for when enter this act, can not do anything
    val NONE_PICKED_APP = QuickApp(CommonApp(NONE_PICK,"","",false), QuickAppType.EMPTY)
    val EMPTY_COMMON_APP = CommonApp("","","",false)

    var gridCnt = 0
    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    var dir = 0
    val pickedApp = ObservableField( NONE_PICKED_APP)
    var curPos = -1

    var insertingApp: CommonApp? = null

    var isMoving = false
    var moveBefPos = -1
    var moveBefDir = -1

    override fun start() {
    }

    fun refreshAppGrid() {
        pickedApp.set(NONE_PICKED_APP)
        gridCnt = repo.preference.getGridCount()
        liveDataApps.value = repo.preference.getQuickApps(dir)

        curPos = -1
    }

    /**
     * set pic app to [empty place] or [in folder]
     */
    fun setPickedApp(quickApp: QuickApp, pos:Int){

        insertingApp?.let {
            saveCommonAppToCurPos(it,pos)
        } ?: let {
            pickedApp.set(quickApp)
            curPos = pos
        }
    }

    /**
     * save common app to [empty place] or [in folder]
     */
    fun saveCommonAppToCurPos(commonApp: CommonApp, pos:Int = curPos) {

        val changedLiveDataApps = liveDataApps.value!!

        if(pickedApp.value().type == QuickAppType.EMPTY)  changedLiveDataApps[pos] = QuickApp(commonApp, QuickAppType.ONE_APP)
        else if(pickedApp.value().type == QuickAppType.FOLDER){
            changedLiveDataApps[pos].cmds.add(commonApp.toSaveString())
            changedLiveDataApps[pos].isPicked = false // for unset select effect

        }
        insertingApp = null
        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
    }

    fun saveQuickAppToCurPos(quickApp: QuickApp, pos:Int = curPos) {

        val changedLiveDataApps = liveDataApps.value!!
            .apply { this[pos]=quickApp }
        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
    }

    fun deleteAppToFromPos(pos:Int = curPos, dir_:Int = dir) {

        val changedLiveDataApps = repo.preference.getQuickApps(dir_).apply {
            this[pos] = QuickApp(EMPTY_COMMON_APP, QuickAppType.EMPTY)
        }
        insertingApp = null
        repo.preference.setQuickApps(changedLiveDataApps ,dir_)
        refreshAppGrid()
    }

    /**
     * 해당 함수 호출 전 clickMove()에서 이동 전의 앱정보를 저장해놓음
     */
    fun moveApp(toPos: Int) {
        val changingQuickApp = repo.preference.getQuickApps(moveBefDir)[moveBefPos]

        if(changingQuickApp.type == QuickAppType.FOLDER && liveDataApps.value!![toPos].type == QuickAppType.FOLDER ) {
            "폴더안에 폴더를 넣을 수 없습니다".toast()
            cancelMoveApp()
            return
        }

        deleteAppToFromPos(moveBefPos,moveBefDir)

        val changedLiveDataApps = liveDataApps.value!!

        if(changedLiveDataApps[toPos].type == QuickAppType.FOLDER) changedLiveDataApps[toPos].cmds.add(changingQuickApp.commonApp.toSaveString())
        else changedLiveDataApps[toPos] = changingQuickApp

        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        cancelMoveApp()
    }

    fun cancelMoveApp() {
        refreshAppGrid()
        curPos = -1
        isMoving = false
        moveBefDir = -1
        moveBefPos = -1
    }

    fun deleteCommonAppFromFolder(cApp: CommonApp) {

        val changedLiveDataApps = liveDataApps.value!!.apply {
            this[curPos].cmds.remove(cApp.toSaveString())
            this[curPos].isPicked = false
        }

        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
    }


    fun clickAdd(){
        toActMsg(
            if(pickedApp.value().type == QuickAppType.FOLDER) MsgType.SELECT_APP_IN_FOLDER
            else MsgType.SELECT_APP
        )
    }

    fun clickDelete(){
        deleteAppToFromPos()
    }

    fun clickMove(){
        isMoving = true
        moveBefPos = curPos
        moveBefDir = dir

        "이동할곳을 선택해주세요".toast()
    }

    fun clickFolder(){
        if(pickedApp.value().type == QuickAppType.EMPTY) {
            saveQuickAppToCurPos(
                QuickApp( EMPTY_COMMON_APP, QuickAppType.FOLDER),
                curPos
            )
        } else if(pickedApp.value().type == QuickAppType.FOLDER) {
            toActMsg(MsgType.OPEN_FOLDER)
        }
    }

    fun clickTwoDepth(){
        "clickTwoDepth".i()
    }

    fun clickExpert(){
        "clickExpert".i()
    }


    fun clickTop() { dir = 0
        refreshAppGrid() }

    fun clickRight() { dir = 1
        refreshAppGrid() }

    fun clickBottom() { dir = 2
        refreshAppGrid() }

    fun clickLeft() { dir = 3
        refreshAppGrid() }

    enum class ArrangeBottoms{
        ADD,
        DELETE,
        MOVE,
        FOLDER,
        TWO_DEPTH,
        EXPERT
    }


}