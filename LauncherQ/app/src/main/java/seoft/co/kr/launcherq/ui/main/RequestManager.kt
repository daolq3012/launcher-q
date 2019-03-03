package seoft.co.kr.launcherq.ui.main

import android.Manifest
import android.support.v7.app.AlertDialog
import seoft.co.kr.launcherq.utill.showDialog
import seoft.co.kr.launcherq.utill.toast

class RequestManager(val activity: MainActivity){

    companion object {
        val REQ_PERMISSIONS = 101
    }

    init {
        reqPermissions()
    }

    fun reqPermissions() {
        activity.requestPermissions(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
            REQ_PERMISSIONS)
    }

    fun showPermissionRequestDialog(){
        AlertDialog.Builder(activity).showDialog(
            title = "Launcher Q 에서 다음 권한이 필요합니다",
            message = "- 파일 입출력 권한 (배경화면 사진 설정 및 가져오기)\n- 카메라 권한 (배경화면 사진 촬영후 설정)\n\n권한요청을 다시 하시겠습니까?",
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                reqPermissions()
            },
            cbNegative = {
                "권한요청이 거부되어 종료합니다".toast()
                activity.finish()
            }
        )
    }

    fun expandStatusbar(){

        try {
            val sbService = activity.application.getSystemService("statusbar") as Any
            val statusbarManager = Class.forName("android.app.StatusBarManager")
            val showSb = statusbarManager.getMethod("expandNotificationsPanel")
            showSb.invoke(sbService)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }



}