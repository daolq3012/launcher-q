package seoft.co.kr.launcherq.data.remote

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okio.Okio
import seoft.co.kr.launcherq.data.callback.FileCallback
import seoft.co.kr.launcherq.data.callback.ThemeDocCallback
import seoft.co.kr.launcherq.data.remote.service.ThemeDocApiServices
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.e
import java.io.File


class RemoteThemeDocRepo {
    val TAG = "RemoteEtcApiRepo#$#"
    private val apiServices = ThemeDocApiServices.create()


    fun getThemeDocById(id:Int, cb: ThemeDocCallback) : Disposable {
        return apiServices.getThemeDocById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                cb.onThemeDocLoad(it)
            }, { error ->
                error.printStackTrace()
                error.message.toString().e(TAG)
                cb.onDataNotAvailable()
            })
    }

    fun getBgImgById(docId:Int, cb: FileCallback) : Disposable{
        return apiServices.getBgImgById(docId)
            .flatMap {
                Observable.create (
                    object : ObservableOnSubscribe<File> {
                        override fun subscribe(subscriber : ObservableEmitter<File>) {
                            val filename = "tmpBgFile"
                            val route = "${App.get.applicationContext.filesDir.absolutePath}/$filename"
                            val destinationFile = File(route)

                            val bufferedSink = Okio.buffer(Okio.sink(destinationFile))
                            bufferedSink.writeAll(it.body()!!.source())
                            bufferedSink.close()

                            subscriber.onNext(destinationFile)
                            subscriber.onComplete()
                        }
                    }
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                cb.onFileLoad(it)
            }, { error ->
                error.printStackTrace()
                error.message.toString().e(TAG)
                cb.onDataNotAvailable()
            })
    }



    fun getFontById(docId:Int,type:String, cb: FileCallback) : Disposable{
        return apiServices.getFontById(docId,type)
            .flatMap {
                Observable.create (
                    object : ObservableOnSubscribe<File> {
                        override fun subscribe(subscriber : ObservableEmitter<File>) {
                            val filename = "$type.ttf"
                            val route = "${App.get.applicationContext.filesDir.absolutePath}/$filename"
                            val destinationFile = File(route)

                            val bufferedSink = Okio.buffer(Okio.sink(destinationFile))
                            bufferedSink.writeAll(it.body()!!.source())
                            bufferedSink.close()

                            subscriber.onNext(destinationFile)
                            subscriber.onComplete()
                        }
                    }
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                cb.onFileLoad(it)
            }, { error ->
                error.printStackTrace()
                error.message.toString().e(TAG)
                cb.onDataNotAvailable()
            })
    }

}