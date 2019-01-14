package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import seoft.co.kr.launcherq.utill.toPixel

class MainCaculator(){

    private var BOUNDARY = 50
    private var rstMidX = 0
    private var rstMidY = 0

    private var startX = 0
    private var startY = 0

    var startViewMarginPointX = 0
    var startViewMarginPointY = 0
    private var optPointForStartViewX = 0
    private var optPointForStartViewY = 0


    var gridViewMarginPointX = 0
    var gridViewMarginPointY = 0
    private var optPointForGridViewX = 0
    private var optPointForGridViewY = 0

    var coordinates =
        arrayOf(
            arrayOf(Point(0,0),Point(0,0)),
            arrayOf(Point(0,0),Point(0,0)),
            arrayOf(Point(0,0),Point(0,0)),
            arrayOf(Point(0,0),Point(0,0))
        )


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

        // optimize open boundary ( *2 는 위치에 따라 가로 혹은 세로 범위를 늘려 접근률을 높이기 위함 )
        val rstNormalBoundary = BOUNDARY.toPixel()
        val rstLargeBoundary = BOUNDARY.toPixel()*2
        val rstDistance = distance.toPixel()

        // calc & save boundary
        coordinates[0].run {
            this[0].x = rstMidX - rstLargeBoundary
            this[0].y = rstMidY - rstDistance - rstNormalBoundary
            this[1].x = rstMidX + rstLargeBoundary
            this[1].y = rstMidY - rstDistance + rstNormalBoundary
        }
        coordinates[1].run {
            this[0].x = rstMidX + rstDistance - rstNormalBoundary
            this[0].y = rstMidY - rstLargeBoundary
            this[1].x = rstMidX + rstDistance + rstNormalBoundary
            this[1].y = rstMidY + rstLargeBoundary
        }
        coordinates[2].run {
            this[0].x = rstMidX - rstLargeBoundary
            this[0].y = rstMidY + rstDistance - rstNormalBoundary
            this[1].x = rstMidX + rstLargeBoundary
            this[1].y = rstMidY + rstDistance + rstNormalBoundary
        }
        coordinates[3].run {
            this[0].x = rstMidX - rstDistance - rstNormalBoundary
            this[0].y = rstMidY - rstLargeBoundary
            this[1].x = rstMidX - rstDistance + rstNormalBoundary
            this[1].y = rstMidY + rstLargeBoundary
        }

    }

    fun calcOpenOneStep(curX: Int, curY: Int, gvSize: Int, screenSize: Point) {

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

    }


}