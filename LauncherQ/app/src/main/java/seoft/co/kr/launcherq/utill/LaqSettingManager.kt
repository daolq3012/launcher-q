package seoft.co.kr.launcherq.utill

import com.google.gson.reflect.TypeToken
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.LaqSetting

class LaqSettingManager{

    val TAG = "LaqSettingManager#$#"

//    lateinit var laqSetting : LaqSetting

    fun getJson(repo: Repo): String {

        with(repo.preference) {
            val laqSetting = LaqSetting(
                getBgDateUse(),
                getBgTimeUse(),
                getBgAmpmUse(),
                getBgDowUse(),
                getBgTextUse(),
                getDrawerColumnNum(),
                getDrawerItemNum(),
                getBgWidgetInfos(),
                arrayOf(
                    getQuickApps(0),
                    getQuickApps(1),
                    getQuickApps(2),
                    getQuickApps(3)
                    ),
                getGridCount(),
                getGridViewSize(),
                getDistance(),
                getMyIconPixel(),
                getBottomBoundary(),
                getTopBoundary()
            )

            return SC.gson.toJson(laqSetting, object : TypeToken<LaqSetting>() {}.type)
        }
    }

    fun setLaqSetting(jsonStr:String, repo:Repo) {
        val laqSetting = SC.gson.fromJson<LaqSetting>(jsonStr, object : TypeToken<LaqSetting>() {}.type) as LaqSetting

        laqSetting.toString().i(TAG)

        laqSetting.let {
            with(repo.preference) {

                setBgDateUse(it.bgDateUse)
                setBgTimeUse(it.bgTimeUse)
                setBgAmpmUse(it.bgAmpmUse)
                setBgDowUse(it.bgDowUse)
                setBgTextUse(it.bgTextUse)
                setDrawerColumnNum(it.drawerColumnNum)
                setDrawerItemNum(it.drawerItemNum)
                setBgWidgetInfos(it.bgWidgetInfos!!)
                setQuickApps(it.quickApps!![0],0)
                setQuickApps(it.quickApps!![1],1)
                setQuickApps(it.quickApps!![2],2)
                setQuickApps(it.quickApps!![3],3)
                setGridCount(it.gridCount)
                setGridViewSize(it.gridViewSize)
                setDistance(it.distance)
                setMyIconPixel(it.myIconPixel)
                setBottomBoundary(it.bottomBoundary)
                setTopBoundary(it.topBoundary)
            }

        }



    }
}