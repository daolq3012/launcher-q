package seoft.co.kr.launcherq

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
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


}
