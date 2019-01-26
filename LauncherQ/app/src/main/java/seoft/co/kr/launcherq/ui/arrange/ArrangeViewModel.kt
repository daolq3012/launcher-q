package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.*
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.toast
import seoft.co.kr.launcherq.utill.value
import java.io.File


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

    var myIconPixel = 0

    override fun start() {
        myIconPixel = repo.preference.getMyIconPixel()
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
        } else if (changingQuickApp.type == QuickAppType.EXPERT && liveDataApps.value!![toPos].type == QuickAppType.FOLDER ) {
            "폴더안에 전문가모드는 폴더안에 넣을 수 없습니다".toast()
            cancelMoveApp()
            return
        }

        if(repo.preference.getQuickApps(moveBefDir)[moveBefPos].hasImg) {
            // file manage ref : https://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
            File(SC.imgDir,"$moveBefDir#$moveBefPos").copyTo(File(SC.imgDir,"$dir#$toPos"),true)
            File(SC.imgDir,"$moveBefDir#$moveBefPos").delete()
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

    fun setHasImage(hasImg : Boolean) {
        val changedLiveDataApps = liveDataApps.value!!.apply {
            this[curPos].hasImg = hasImg
            this[curPos].isPicked = false
        }
        repo.preference.setQuickApps(changedLiveDataApps ,dir)
        refreshAppGrid()
    }


    fun saveExpertAppToCurPosForTest(pos:Int = curPos) {

        val testCnt =3

        if(testCnt == 1) {

        } else if(testCnt == 2) {


            val changedLiveDataApps = liveDataApps.value!!
                .apply {
                    this[pos] = QuickApp(EMPTY_COMMON_APP,
                        QuickAppType.EXPERT,
                        expert = Expert(
                            CustomIntent(
                                name="크롬",
                                action="android.intent.action.MAIN",
                                categorys = arrayListOf("android.intent.category.LAUNCHER"),
                                addFlag = arrayListOf(268435456, 2097152),
                                customComponentName =  CustomComponentName("com.android.chrome","com.google.android.apps.chrome.Main")
                            ),
                            arrayListOf(
                                CustomIntent(
                                    name="네이버",
                                    action="android.intent.action.VIEW",
                                    uriData = "http://naver.com"
                                ),
                                CustomIntent(
                                    name="구글",
                                    action="android.intent.action.VIEW",
                                    uriData = "http://google.com"
                                ),
                                CustomIntent(
                                    name="스텍오버플로우",
                                    action="android.intent.action.VIEW",
                                    uriData = "https://stackoverflow.com"
                                )
                            )
                        )
                    )

                }
            repo.preference.setQuickApps(changedLiveDataApps ,dir)
            refreshAppGrid()


        }
        else if(testCnt == 3) {


            val changedLiveDataApps = liveDataApps.value!!
                .apply {
                    this[pos] = QuickApp(CommonApp("seoft.co.kr.chatfactory",
                        "챗팩",
                        "seoft.co.kr.chatfactory.ui.splash.SplashActivity"
                        ),
                        QuickAppType.EXPERT,
                        expert = Expert(null,
                            arrayListOf(
                                CustomIntent(
                                    name="엄마",
                                    action="android.intent.action.DIAL",
                                    uriData = "tel:0123456789"
                                ),
                                CustomIntent(
                                    name="bb",
                                    action="android.intent.action.DIAL",
                                    uriData = "tel:789456123"
                                ),
                                CustomIntent(
                                    name="cc",
                                    action="android.intent.action.DIAL",
                                    uriData = "tel:11111111"
                                )
                            )
                        )
                    )

                }
            repo.preference.setQuickApps(changedLiveDataApps ,dir)
            refreshAppGrid()


        }

    }

    /**
     * Expert mode situaction
     *
     * 1. 기존앱 + 2스탭 설정 ( ex
     * 2. 빈 앱 + 2스탭 설정 ( ex 설정 집합들 바로가기,
     * 3. 설정앱 + 2스탭 설정 ( ex
     * 4. 설정앱 ( ex 설정 바로가기,
     */
    /**
     * not null -> use this
     * null check commonApp was empty ->
     *                                  not empty -> use that
     *                                  empty -> not use
     */



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
//        saveExpertAppToCurPosForTest()
//        expertSetting.set(true)
        toActMsg(MsgType.OPEN_EXPERT_STATUS)
    }
//
//    one step
//
//    two step (+)
//    aaaaaaaaaaaa(수정)(삭제)
//    aaaaaaaaaaaa(수정)(삭제)
//    aaaaaaaaaaaa(수정)(삭제)
//    aaaaaaaaaaaa(수정)(삭제)
//    aaaaaaaaaaaa(수정)(삭제)
//
//    요런식으로 다이얼로그 후
//
//    expertSetting.set(true) 이거 진행 ㄱㄱ

    fun clickIcon(){
        "clickIcon".i()
        toActMsg(MsgType.OPEN_ICON_SETTER, pickedApp.value().hasImg)
    }

    fun clickCancelExpertSetting(){
        refreshAppGrid()
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
        EXPERT,
        ICON
    }


}