package seoft.co.kr.launcherq.ui.adjust

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.callback.FileCallback
import seoft.co.kr.launcherq.data.model.ThemeDoc
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.plusAssign
import java.io.File

class AdjustActivity : AppCompatActivity() {

    /************************
     * Dependency with Launcher Q
     */
    val THEHE_INFO = "THEHE_INFO"
    /************************/

    val TAG = "AdjustActivity#$#"

    lateinit var themeInfoJson : String
    lateinit var themeDoc: ThemeDoc

    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust)

        themeInfoJson = intent.getStringExtra(THEHE_INFO)
        themeDoc = SC.gson.fromJson<ThemeDoc>(themeInfoJson, object : TypeToken<ThemeDoc>(){}.type)
        themeDoc.toString().i(TAG)
    }

    override fun onResume() {
        super.onResume()

        compositeDisposable += Repo.themeDocApi.getBgImgById(themeDoc.id,
            object: FileCallback {
                override fun onFileLoad(file: File) {
                    val bitmap = BitmapFactory.decodeFile(file.path)
                    Repo.backgroundRepo.saveBitmap( bitmap, App.get )
                    SC.needResetBgSetting = true

                    if(file.isFile) file.delete()
                    "onFileLoad".i(TAG)
//                    isLoading.set(false)
                }

                override fun onDataNotAvailable() {
                    "onDataNotAvailable".i(TAG)
//                    isLoading.set(false)
                }
            }
        )


    }

    override fun onPause() {
        super.onPause()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }



}
