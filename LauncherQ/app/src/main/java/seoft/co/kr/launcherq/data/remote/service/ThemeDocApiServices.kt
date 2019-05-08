package seoft.co.kr.launcherq.data.remote.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import seoft.co.kr.launcherq.BuildConfig
import seoft.co.kr.launcherq.data.model.ThemeDoc

interface ThemeDocApiServices {

    @GET("themeDocById/{docId}")
    fun getThemeDocById(@Path("docId") docId: Int): Observable<ThemeDoc>

    // ref : https://www.codexpedia.com/android/retrofit-2-and-rxjava-for-file-downloading-in-android/
    @GET("image/bg/{docId}")
    @Streaming
    fun getBgImgById(@Path("docId") docId: Int): Observable<Response<ResponseBody>>

    @GET("font/{docId}/{type}")
    @Streaming
    fun getFontById(@Path("docId") docId: Int,@Path("type") type: String): Observable<Response<ResponseBody>>

    companion object Factory {
        fun create(): ThemeDocApiServices {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.SERVER_URL)
                .build()

            return retrofit.create(ThemeDocApiServices::class.java)
        }
    }
}