package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import seoft.co.kr.launcherq.utill.toPixel

class MainCaculator{

    val TAG = "MainCaculator#$#"

    private var BOUNDARY = 50
    private var rstMidX = 0
    private var rstMidY = 0

    private var startX = 0
    private var startY = 0

    var startViewMarginPointX = 0
    var startViewMarginPointY = 0
    private var optPointForStartViewX = 0
    private var optPointForStartViewY = 0


    // use this params when open one step gridview & two step after long click in open two step
    var gridViewMarginPointX = 0
    var gridViewMarginPointY = 0

    private var optPointForGridViewX = 0
    private var optPointForGridViewY = 0

    var starterCoordinates = Array(4, { arrayOf(Point(0,0),Point(0,0))})

    var oneStepCoordinates = Array(16, { arrayOf(Point(0,0),Point(0,0))})

    fun calcOpenTouchStart(
        startX_: Int,
        startY_: Int,
        distance: Int,
        screenSize: Point
    ) {
        optPointForStartViewX = 0
        optPointForStartViewY = 0

        startX = startX_
        startY = startY_


        // Add out interval when startView leave device screen
        // when out left or right
        if(startX + distance.toPixel() > screenSize.x ) optPointForStartViewX = startX + distance.toPixel() - screenSize.x
        else if(startX - distance.toPixel() < 0 ) optPointForStartViewX = startX - distance.toPixel()

        // when out top or bottom
        if(startY + distance.toPixel() > screenSize.y ) optPointForStartViewY = startY + distance.toPixel() - screenSize.y
        else if(startY - distance.toPixel() < 0 ) optPointForStartViewY = startY - distance.toPixel()

        // calc marginPoint for show starterview on cur finger point
        startViewMarginPointX = startX - distance.toPixel() - optPointForStartViewX
        startViewMarginPointY = startY - distance.toPixel() - optPointForStartViewY

        // starterview has four direction(top, bottom, left, right) UI
        ///////// starterview view draw step was done /////////


        // calc rst mid point for catch open boundary point
        rstMidX = startX - optPointForStartViewX
        rstMidY = startY - optPointForStartViewY

/////////// logging for catch error
//
//        "screenSize $screenSize".i(TAG)
//
//        "X $optPointForStartViewX".i(TAG)
//        "Y $optPointForStartViewY".i(TAG)
//
//        "startX $startX".i(TAG)
//        "startY $startY".i(TAG)
//
//        "rstMidX $rstMidX".i(TAG)
//        "rstMidX $rstMidY".i(TAG)
//
//        "startViewMarginPointX $startViewMarginPointX".i(TAG)
//        "startViewMarginPointY $startViewMarginPointY".i(TAG)
//
//        "distance.toPixel() ${distance.toPixel()}".i(TAG)
//
//        " ".i(TAG)
//        " ".i(TAG)
//


        // optimize open boundary ( *2 는 위치에 따라 가로 혹은 세로 범위를 늘려 접근률을 높이기 위함 )
        val rstNormalBoundary = BOUNDARY.toPixel()
        val rstLargeBoundary = BOUNDARY.toPixel()*2
        val rstDistance = distance.toPixel()

        // calc & save move arrive boundary
        starterCoordinates[0].run {
            this[0].x = rstMidX - rstLargeBoundary
            this[0].y = rstMidY - rstDistance - rstNormalBoundary
            this[1].x = rstMidX + rstLargeBoundary
            this[1].y = rstMidY - rstDistance + rstNormalBoundary
        }
        starterCoordinates[1].run {
            this[0].x = rstMidX + rstDistance - rstNormalBoundary
            this[0].y = rstMidY - rstLargeBoundary
            this[1].x = rstMidX + rstDistance + rstNormalBoundary
            this[1].y = rstMidY + rstLargeBoundary
        }
        starterCoordinates[2].run {
            this[0].x = rstMidX - rstLargeBoundary
            this[0].y = rstMidY + rstDistance - rstNormalBoundary
            this[1].x = rstMidX + rstLargeBoundary
            this[1].y = rstMidY + rstDistance + rstNormalBoundary
        }
        starterCoordinates[3].run {
            this[0].x = rstMidX - rstDistance - rstNormalBoundary
            this[0].y = rstMidY - rstLargeBoundary
            this[1].x = rstMidX - rstDistance + rstNormalBoundary
            this[1].y = rstMidY + rstLargeBoundary
        }

    }

    fun calcOpenOneStep(curX: Int, curY: Int, gvSize: Int, screenSize: Point, gridCnt:Int) {

        optPointForGridViewX = 0
        optPointForGridViewY = 0

        // Add out interval when gridview leave device screen
        // when out left or right
        if(curX + gvSize.toPixel()/2 > screenSize.x ) optPointForGridViewX = curX + gvSize.toPixel()/2 - screenSize.x
        else if(curX - gvSize.toPixel()/2 < 0 ) optPointForGridViewX = curX - gvSize.toPixel()/2

        // when out top or bottom
        if(curY + gvSize.toPixel()/2 > screenSize.y ) optPointForGridViewX = curY + gvSize.toPixel()/2 - screenSize.y
        else if(curY - gvSize.toPixel()/2 < 0 ) optPointForGridViewY = curY - gvSize.toPixel()/2

        // calc marginPoint for show starterview on cur finger point
        gridViewMarginPointX = curX - gvSize.toPixel()/2 - optPointForGridViewX
        gridViewMarginPointY = curY - gvSize.toPixel()/2 - optPointForGridViewY

        val xOne = gvSize.toPixel() / gridCnt
        val xTwo = gvSize.toPixel() * 2 / gridCnt
        val xThree = gvSize.toPixel() * 3 / gridCnt
        val xFour = gvSize.toPixel() * 4 / gridCnt

        // calc & save touch up boundary
        if(gridCnt == 3) {
            with(oneStepCoordinates){
                for (i in 0 until 9) {
                    if(i%3==0) {
                        this[i][0].x = gridViewMarginPointX
                        this[i][1].x = gridViewMarginPointX + xOne
                    } else if(i%3==1) {
                        this[i][0].x = gridViewMarginPointX + xOne
                        this[i][1].x = gridViewMarginPointX + xTwo
                    } else {
                        this[i][0].x = gridViewMarginPointX + xTwo
                        this[i][1].x = gridViewMarginPointX + xThree
                    }
                    if(i < 3) {
                        this[i][0].y = gridViewMarginPointY
                        this[i][1].y = gridViewMarginPointY + xOne
                    } else if(i < 6) {
                        this[i][0].y = gridViewMarginPointY + xOne
                        this[i][1].y = gridViewMarginPointY + xTwo
                    } else {
                        this[i][0].y = gridViewMarginPointY + xTwo
                        this[i][1].y = gridViewMarginPointY + xThree
                    }
                }
            }
        }

        if(gridCnt == 4) {
            with(oneStepCoordinates){
                for (i in 0 until 16) {
                    if(i%4==0) {
                        this[i][0].x = gridViewMarginPointX
                        this[i][1].x = gridViewMarginPointX + xOne
                    } else if(i%4==1) {
                        this[i][0].x = gridViewMarginPointX + xOne
                        this[i][1].x = gridViewMarginPointX + xTwo
                    } else if(i%4==2) {
                        this[i][0].x = gridViewMarginPointX + xTwo
                        this[i][1].x = gridViewMarginPointX + xThree
                    } else {
                        this[i][0].x = gridViewMarginPointX + xThree
                        this[i][1].x = gridViewMarginPointX + xFour
                    }
                    if(i < 4) {
                        this[i][0].y = gridViewMarginPointY
                        this[i][1].y = gridViewMarginPointY + xOne
                    } else if(i < 8) {
                        this[i][0].y = gridViewMarginPointY + xOne
                        this[i][1].y = gridViewMarginPointY + xTwo
                    } else if(i < 12) {
                        this[i][0].y = gridViewMarginPointY + xTwo
                        this[i][1].y = gridViewMarginPointY + xThree
                    } else {
                        this[i][0].y = gridViewMarginPointY + xThree
                        this[i][1].y = gridViewMarginPointY + xFour
                    }
                }
            }
        }


    }

    fun calcInboundOneStep(curX:Int,curY:Int,gridCnt: Int) :Int {

        if( oneStepCoordinates[0][0].x <= curX && curX <= oneStepCoordinates[gridCnt*gridCnt-1][1].x &&
            oneStepCoordinates[0][0].y <= curY && curY <= oneStepCoordinates[gridCnt*gridCnt-1][1].y ) {

            for(i in 0 until  gridCnt*gridCnt) {
                if( oneStepCoordinates[i][0].x <= curX && curX <= oneStepCoordinates[i][1].x &&
                    oneStepCoordinates[i][0].y <= curY && curY <= oneStepCoordinates[i][1].y ) {
                    return i
                }
            }
        }
        return -1
    }


    fun calcOpenTwoStep(curX: Int, curY: Int, screenSize: Point, width:Int,height:Int):Point {
        var optPointX = 0
        var optPointY = 0

        if (curX + width / 2 > screenSize.x) optPointX = curX + width / 2 - screenSize.x
        else if (curX - width / 2 < 0) optPointX = curX - width / 2
        if (curY + height / 2 > screenSize.y) optPointX = curY + height / 2 - screenSize.y
        else if (curY - height / 2 < 0) optPointY = curY - height / 2
        return Point( curX - width / 2 - optPointX, curY - height / 2 - optPointY )
    }

}