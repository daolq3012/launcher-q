package seoft.co.kr.launcherq.ui.arrange

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_arrange_folder.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.utill.toast

class ArrangeFolderDialog(context: Context, val appList: List<String>, val cb:(CommonApp)->Unit ) : Dialog(context) {

    val TAG = "ArrangeFolderDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_arrange_folder)

        initView()

        "폴더에서 제거할 앱을 선택하세요".toast()
    }

    fun initView(){
        val adapter = ArrangeFolderAdapter(context, appList) {
            cb.invoke(it)
            dismiss()
        }

        lvFolderApps.adapter = adapter

    }

}