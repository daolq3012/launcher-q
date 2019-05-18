package seoft.co.kr.launcherq.utill

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import seoft.co.kr.launcherq.R

class ShutdownConfigAdminReceiver : DeviceAdminReceiver() {
    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context, R.string.fail_to_bring_admin_permission.TRANS(), Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context, R.string.success_to_bring_admin_permission.TRANS(), Toast.LENGTH_SHORT).show()
    }
}
