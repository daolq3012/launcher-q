package seoft.co.kr.launcherq.ui.adjust

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.callback.FileCallback
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.Info
import seoft.co.kr.launcherq.data.model.ThemeDoc
import seoft.co.kr.launcherq.ui.setting.FontActivity.Companion.LOAD_FONT_FILE
import seoft.co.kr.launcherq.utill.*
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

    private var isDone = 0 // themeDoc.themeInfos.size(5) is done, do next step in font step

    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust)

        themeInfoJson = intent.getStringExtra(THEHE_INFO)

        "themeInfoJson $themeInfoJson".i()

        themeDoc = SC.gson.fromJson<ThemeDoc>(themeInfoJson, object : TypeToken<ThemeDoc>(){}.type)
        themeDoc.toString().i(TAG)


    }

    override fun onResume() {
        super.onResume()
        adjustBgImage()
    }

    fun adjustBgImage(){
        "adjustBgImage".i()

        compositeDisposable += Repo.themeDocApi.getBgImgById(themeDoc.id,
            object: FileCallback {
                override fun onFileLoad(file: File) {
                    val bitmap = BitmapFactory.decodeFile(file.path)
                    Repo.backgroundRepo.saveBitmap( bitmap, App.get )

                    if(file.isFile) file.delete()
                    "getBgImgById onFileLoad".i(TAG)
                    adjustFont()
                }

                override fun onDataNotAvailable() {
                    "onDataNotAvailable".i(TAG)
                    R.string.fail_to_load_bg.TRANS().toast()
                    finish()
                }
            }
        )
    }

    fun adjustFont(){
        "adjustFont".i()

        themeDoc.themeInfos?.forEach {
            if(it.font == LOAD_FONT_FILE){
                val type = it.type.getWidget().getStr
                compositeDisposable += Repo.themeDocApi.getFontById(themeDoc.id, type ,
                    object: FileCallback {
                        override fun onFileLoad(file: File) {
                            "getFontById onFileLoad $type".i(TAG)
                            isDone++
                            checkNextStepFromFontStep()
                        }
                        override fun onDataNotAvailable() {
                            "onDataNotAvailable".i(TAG)
                            R.string.fail_to_load_font.TRANS().toast()
                            finish()
                        }
                    }
                )
            } else {
                isDone++
                checkNextStepFromFontStep()
            }
        }
    }

    fun checkNextStepFromFontStep(){

        if(themeDoc.themeInfos == null )
            R.string.fail_to_load_font.TRANS().toast()

        if(isDone ==  themeDoc.themeInfos!!.size) {
            adjustRemainder()
        }
    }

    fun adjustRemainder(){
        "adjustRemainder".i()

        val bgwi = BackgroundWidgetInfos(
            themeDoc
                .themeInfos?.map {
                Info(it.etc,it.size,it.color,it.posX,it.posY,it.font)
            }!!.toTypedArray()
        )
        Repo.preference.setBgWidgetInfos(bgwi)
        SC.needResetBgSetting = true
        R.string.success_to_set_theme.TRANS().toast()
        finish()
    }

    override fun onPause() {
        super.onPause()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

}
