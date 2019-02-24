package seoft.co.kr.launcherq.utill

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageReceiver: BroadcastReceiver(){

    val TAG = "PackageReceiver#$#"

    override fun onReceive(context: Context, intent: Intent) {

        val packageName = intent.data.schemeSpecificPart
        val action = intent.action

        if (action == Intent.ACTION_PACKAGE_ADDED) {
            "ACTION_PACKAGE_ADDED : $packageName".i(TAG)
        } else if (action == Intent.ACTION_PACKAGE_REMOVED) {
            "ACTION_PACKAGE_REMOVED : $packageName".i(TAG)
        }else if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
            "ACTION_PACKAGE_FULLY_REMOVED : $packageName".i(TAG)
        }

    }

}