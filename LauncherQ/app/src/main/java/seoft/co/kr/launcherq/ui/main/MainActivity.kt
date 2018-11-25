package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityMainBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.main.RequestManager.Companion.REQ_PERMISSIONS
import seoft.co.kr.launcherq.utill.observeActMsg

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    private lateinit var binding:ActivityMainBinding
    private lateinit var vm : MainViewModel
    private lateinit var requestManager : RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        vm = ViewModelProviders.of(this, MainViewModel(Repo).create()).get(MainViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.START_ACTIVITY -> startActivity(Intent(applicationContext,vm.msg as Class<*>))

            }
        })

        requestManager = RequestManager(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ_PERMISSIONS && grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
            vm.start()
        } else {
            requestManager.showPermissionRequestDialog()
        }
    }

}
