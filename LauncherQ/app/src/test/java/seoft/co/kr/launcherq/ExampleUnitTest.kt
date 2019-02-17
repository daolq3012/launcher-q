package seoft.co.kr.launcherq

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun ktTest(){
        "=== ktTest ===".p()

        var curList = mutableListOf(
            Apple("AA",1),
//            Apple("REMOVE",2),
            Apple("BB",3)
        )

        var instList = mutableListOf(
            Apple("AA",1),
            //remove
            Apple("BB",2)
//            Apple("ADD1",4),
//            Apple("ADD2",5)
        )

        var isChanged = false
        var tmpRst = false

        curList = curList
            .filter { cApp ->
                tmpRst = instList
                    .any{ iApp ->
                        iApp.name == cApp.name
                    }
                if(!tmpRst) isChanged = true
                tmpRst

            }
            .toMutableList()
            .apply {
                this.addAll(
                    instList
                        .filter { cApp ->
                            tmpRst = !curList
                                .any{ iApp ->
                                    iApp.name == cApp.name
                                }
                            if(tmpRst) isChanged = true
                            tmpRst

                        }
                        .toList()
                )
            }

        curList.toString().p() // aa bb
        isChanged.toString().p()
//
//        curList.addAll(
//            instList
//                .filter { iApp ->
//                    !curList
//                        .any{ cApp ->
//                            iApp.name == cApp.name
//                        }
//                }
//                .toList()
//        )
//
//        curList.toString().p() // aa bb add1 add2





    }

    @Test
    fun ktTest2(){
        "=== ktTest2 ===".p()


        var curList = mutableListOf(
            Apple("AA",0),
            Apple("BB",1),
            Apple("CC",2),
            Apple("DD",3),
            Apple("EE",4),
            Apple("FF",5)
        )

        val from = 2
        val to = 2

        curList.add(to, curList.get(from))
        curList.removeAt(
            if(from < to) from else from + 1
        )

        curList.toString().p()


    }

    data class Apple(var name:String, var price:Int)

    fun String.p(){println(this)}
    fun Int.p(){println(this.toString())}

    @Test
    fun ktTest3(){
        val ll = mutableListOf("AA","BB","CC")

        ll.remove("AA")

        ll.forEach {
            println(it)
        }

    }

    @Test
    fun ktTest4(){
//        val uri = Uri.parse("content://seoft.co.kr.launcherq.utill.CommandContentProvider/COMMAND/15")
//
//        with(uri) {
//            println(scheme)
//            println(schemeSpecificPart)
//            println(authority)
//            println(userInfo)
//            println(host)
//            println(port)
//            println(path)
//            println(query)
//            println(fragment)
//        }
    }

}


