package seoft.co.kr.launcherq.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import seoft.co.kr.launcherq.utill.showDialog
import seoft.co.kr.launcherq.utill.toast

class RequestManager(val activity: MainActivity){

    val TAG = "RequestManager#$#"

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


    /**
     * this three method using in onResume, not creator of this class
     */
    fun hasSystemPermission() : Boolean {
        return Settings.System.canWrite(activity)
    }

    fun reqSystemPermissions() {
        val settingsCanWrite = Settings.System.canWrite(activity)
        if (!settingsCanWrite) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                .apply {
                    data = Uri.parse("package:seoft.co.kr.launcherq") }
            activity.startActivity(intent)
        }
    }

    fun showSystemPermissionRequestDialog(){

        // TODO fix bug of call twice
        AlertDialog.Builder(activity).showDialog(
            title = "Launcher Q 에서 다음 권한이 필요합니다",
            message = "- 화면 잠금시간 임시 제어(홈 화면 더블 탭시 화면꺼짐)\n- 화면 밝기 임시 제어(홈 화면 더블 탭시 화면꺼짐\n\n권한요청을 다시 하시겠습니까?",
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                reqSystemPermissions()
            },
            cbNegative = {
                "권한요청이 거부되어 종료합니다".toast()
                activity.finish()
            }
        )
    }


}