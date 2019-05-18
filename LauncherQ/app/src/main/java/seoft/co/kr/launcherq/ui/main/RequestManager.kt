package seoft.co.kr.launcherq.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.TRANS
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
            REQ_PERMISSIONS)
    }

    fun showPermissionRequestDialog(){
        AlertDialog.Builder(activity).showDialog(
            title = R.string.need_permission.TRANS(),
            message = R.string.need_permission_content.TRANS(),
            postiveBtText = R.string.ok.TRANS(),
            negativeBtText = R.string.cancel.TRANS(),
            cbPostive = {
                reqPermissions()
            },
            cbNegative = {
                R.string.quit_app_refuse.TRANS().toast()
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

    val systemPermissionRequestDialog = AlertDialog.Builder(activity)
        .setTitle(R.string.need_permission.TRANS())
        .setMessage(R.string.need_permission_conent2.TRANS())
        .setPositiveButton(R.string.ok.TRANS(),({ _, _ ->
            reqSystemPermissions()
        }))
        .setNegativeButton(R.string.cancel.TRANS(), ({ _, _ ->
            R.string.quit_app_refuse.TRANS().toast()
            activity.finish()
        }))
        .setCancelable(false)

}