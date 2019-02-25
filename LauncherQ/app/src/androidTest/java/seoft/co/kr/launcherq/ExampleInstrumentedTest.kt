package seoft.co.kr.launcherq

import android.net.Uri
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.i


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val TAG = "ExampleTest#$#"

    @Test
    fun useAppContext() {
        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getTargetContext()
//        assertEquals("seoft.co.kr.launcherq", appContext.packageName)

//        val quickApps = mutableListOf(
//            QuickApp(CommonApp("AAA","AAA","AAA",true), QuickAppType.EMPTY ),
//            QuickApp(CommonApp("BBB","BBB","BBB",true), QuickAppType.EMPTY),
//            QuickApp(CommonApp("CCC","CCC","CCC",true), QuickAppType.EMPTY)
//        )
//
//        Repo.preference.setQuickApps(quickApps,0)
//
//        val rst = Repo.preference.getQuickApps(0)
//
//        rst.map {
//            println(it.toString())
//            it.toString().i(TAG)
//        }
    }


    @Test
    fun t2() {
        val p1 = Person()
        val p2 = Person("BB")
        val p3 = Person("BB",33)
        val p4 = Person(age = 44)

        p1.toString().i(TAG)
        p2.toString().i(TAG)
        p3.toString().i(TAG)
        p4.toString().i(TAG)



    }

    data class Person(
        var name :String = "AA",
        var age :Int= 22
    )

    @Test
    fun t3(){
        CAppException.values().forEach { it.get.i(TAG) }
    }

    @Test
    fun t4(){
        val uri = Uri.parse("content://seoft.co.kr.launcherq.utill.CommandContentProvider/COMMAND/15?pkgName=abc")

        with(uri) {
            (scheme).i(TAG)
            (schemeSpecificPart).i(TAG)
            (authority).i(TAG)
            (host).i(TAG)
            (port).i(TAG)
            (path).i(TAG)
            (query).i(TAG)
            (getQueryParameter("pkgName")).i(TAG)
//            (fragment).i(TAG)
        }

        /**
         * RESULT IS
         * content
         * //seoft.co.kr.launcherq.utill.CommandContentProvider/COMMAND/15
         * seoft.co.kr.launcherq.utill.CommandContentProvider
         * seoft.co.kr.launcherq.utill.CommandContentProvider
         * -1
         * /COMMAND/15
         * pkgName=abc
         * abc
         */


    }

    @Test
    fun t5(){

        val pkgMng = App.get.getPackageManager()

        val app = pkgMng.getApplicationInfo("com.google.android.music", 0)

        val label = pkgMng.getApplicationLabel(app)

        label.i(TAG)

    }

}
