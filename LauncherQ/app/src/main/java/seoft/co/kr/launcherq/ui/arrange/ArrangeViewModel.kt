package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.toast
import seoft.co.kr.launcherq.utill.value


class ArrangeViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "ArrangeViewModel#$#"

    companion object {
        val SPLITTER = "#$#"
        val NONE_PICK = "NONE_PICK"
    }

    // for when enter this act, can not do anything
    val NONE_PICKED_APP = QuickApp(CommonApp(NONE_PICK,"","",false), QuickAppType.EMPTY, mutableListOf() )
    val EMPTY_COMMON_APP = CommonApp("","","",false)

    var gridCnt = 3
    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    var dir = 0
    val pickedApp = ObservableField( NONE_PICKED_APP)
    var arrayPos = -1

    var insertingApp: CommonApp? = null

    var isMoving = false
    var moveBefPos = -1
    var moveBefDir = -1

    override fun start() {
    }

    fun refreshAppGrid() {
        pickedApp.set(NONE_PICKED_APP)
        gridCnt = repo.preference.getGridCount()
//        liveDataApps.value = emptyList<QuickApp>().toMutableList() // for refresh when insert app into folder
        liveDataApps.value = repo.preference.getQuickApps(dir)
    }

    /**
     * set pic app to [empty place] or [in folder]
     */
    fun setPickedApp(quickApp: QuickApp, pos:Int){

        insertingApp?.let {
            saveCommonAppToCurPos(it,pos)
        } ?: let {
            pickedApp.set(quickApp)
            arrayPos = pos
        }
    }

    /**
     * save common app to [empty place] or [in folder]
     */
    fun saveCommonAppToCurPos(commonApp: CommonApp, pos:Int = arrayPos) {

        val changedLiveDataApps = liveDataApps.value!!

        if(pickedApp.value().type == QuickAppType.EMPTY)  changedLiveDataApps[pos] = QuickApp(commonApp, QuickAppType.ONE_APP, mutableListOf() )
        else if(pickedApp.value().type == QuickAppType.FOLDER){
            changedLiveDataApps[pos] = changedLiveDataApps[pos].apply {
                with(commonApp) {
                    cmds.add("${pkgName}${SPLITTER}${detailName}${SPLITTER}${label}") // one app save to string (pkgname#$#detailname#$#label)
                }
            }
            changedLiveDataApps[pos].commonApp.isHide = false // for unset select effect

        }
        insertingApp = null
        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
        arrayPos = -1

    }

    fun saveQuickAppToCurPos(quickApp: QuickApp, pos:Int = arrayPos) {

        val changedLiveDataApps = liveDataApps.value!!
        changedLiveDataApps[pos] = quickApp
        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
        arrayPos = -1
    }

    fun deleteAppToFromPos(pos:Int = arrayPos, dir_:Int = dir) {

        val changedLiveDataApps = repo.preference.getQuickApps(dir_)
        changedLiveDataApps[pos] = QuickApp(EMPTY_COMMON_APP, QuickAppType.EMPTY, mutableListOf() )
        insertingApp = null
        repo.preference.setQuickApps(changedLiveDataApps ,dir_)
        refreshAppGrid()
        arrayPos = -1
    }

    /**
     * 해당 함수 호출 전 clickMove()에서 이동 전의 앱정보를 저장해놓음
     */
    fun moveApp(toPos: Int) {
        val changingQuickApp = repo.preference.getQuickApps(moveBefDir)[moveBefPos]
        deleteAppToFromPos(moveBefPos,moveBefDir)

        val changedLiveDataApps = liveDataApps.value!!

        if(changedLiveDataApps[toPos].type == QuickAppType.FOLDER) {
            changedLiveDataApps[toPos] = changedLiveDataApps[toPos].apply {
                with(changingQuickApp.commonApp) {
                    // one app save to string (pkgname#$#detailname#$#label)
                    cmds.add("${pkgName}${SPLITTER}${detailName}${SPLITTER}${label}")
                }
            }
        } else {
            changedLiveDataApps[toPos] = changingQuickApp
        }

        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
        arrayPos = -1
        isMoving = false
    }

    fun cancelMoveApp() {
        refreshAppGrid()
        arrayPos = -1
        moveBefDir = -1
        isMoving = false
    }


    fun clickAdd(){
        "clickAdd".i()
        toActMsg(MsgType.SELECT_APP)
    }

    fun clickDelete(){
        "clickDelete".i()
        deleteAppToFromPos()
    }

    fun clickMove(){
        "clickMove".i()
        isMoving = true
        moveBefPos = arrayPos
        moveBefDir = dir

        "이동할곳을 선택해주세요".toast()
    }

    fun clickFolder(){
        "clickFolder".i()
        if(pickedApp.value().type == QuickAppType.EMPTY) {
            saveQuickAppToCurPos(
                QuickApp(
                    EMPTY_COMMON_APP,
                    QuickAppType.FOLDER,
                    mutableListOf() ),
                arrayPos
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