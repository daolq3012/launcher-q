package seoft.co.kr.launcherq.ui.setting

import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.TRANS

class BgWidgetContent {

    val widgetClassContents = arrayOf(
        WidgetClassContent(R.string.widget_explain_1.TRANS(),R.string.widget_explain_11.TRANS()),
        WidgetClassContent(R.string.widget_explain_2.TRANS(),R.string.widget_explain_22.TRANS()),
        WidgetClassContent(R.string.widget_explain_3.TRANS(),R.string.widget_explain_33.TRANS()),
        WidgetClassContent(R.string.widget_explain_4.TRANS(),R.string.widget_explain_44.TRANS()),
        WidgetClassContent(R.string.widget_explain_5.TRANS(), R.string.widget_explain_55.TRANS())

    )

    data class WidgetClassContent(val title:String, val explain:String)

}