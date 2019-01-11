package seoft.co.kr.launcherq

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
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

        val quickApps = mutableListOf(
            QuickApp(CommonApp("AAA","AAA","AAA",true), QuickAppType.EMPTY, mutableListOf()),
            QuickApp(CommonApp("BBB","BBB","BBB",true), QuickAppType.EMPTY, mutableListOf()),
            QuickApp(CommonApp("CCC","CCC","CCC",true), QuickAppType.EMPTY, mutableListOf())
        )

        Repo.preference.setQuickApps(quickApps,0)

        val rst = Repo.preference.getQuickApps(0)

        rst.map {
            println(it.toString())
            it.toString().i(TAG)
        }



    }
}
