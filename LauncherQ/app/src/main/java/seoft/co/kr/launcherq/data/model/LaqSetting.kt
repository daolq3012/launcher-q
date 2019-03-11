package seoft.co.kr.launcherq.data.model

data class LaqSetting(var bgDateUse :Boolean = true,
                      var bgTimeUse :Boolean = true,
                      var bgAmpmUse :Boolean = true,
                      var bgDowUse :Boolean = true,
                      var bgTextUse :Boolean = true,
                      var drawerColumnNum :Int = 0,
                      var drawerItemNum :Int = 0,
                      var bgWidgetInfos :BackgroundWidgetInfos? = null,
                      var quickApps :Array<MutableList<QuickApp>>? = null,
                      var gridCount :Int = 0,
                      var gridViewSize :Int = 0,
                      var distance :Int = 0,
                      var myIconPixel :Int = 0,
                      var bottomBoundary :Int = 0,
                      var topBoundary :Int = 0
)