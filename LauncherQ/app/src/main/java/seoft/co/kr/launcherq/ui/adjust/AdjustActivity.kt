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
        adjustBgImage()
    }

    fun adjustBgImage(){

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
                    "배경을 불러오는대 실패하였습니다".toast()
                    finish()
                }
            }
        )
    }

    fun adjustFont(){
        themeDoc.themeInfos?.forEach {
            if(it.font == LOAD_FONT_FILE){
                val type = it.type.getWidget().getStr
                compositeDisposable += Repo.themeDocApi.getFontById(themeDoc.id, type ,
                    object: FileCallback {
                        override fun onFileLoad(file: File) {
                            "getFontById onFileLoad $type".i(TAG)
                            adjustRemainder()
                        }
                        override fun onDataNotAvailable() {
                            "onDataNotAvailable".i(TAG)
                            "폰트를 불러오는대 실패하였습니다".toast()
                            finish()
                        }
                    }
                )
            }
        }

    }

    fun adjustRemainder(){
        val bgwi = BackgroundWidgetInfos(
            themeDoc
                .themeInfos?.map {
                Info(it.etc,it.size,it.color,it.posX,it.posY,it.font)
            }!!.toTypedArray()
        )
        Repo.preference.setBgWidgetInfos(bgwi)
        SC.needResetBgSetting = true
        "테마 적용 완료".toast()
        finish()
    }

    override fun onPause() {
        super.onPause()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

}
