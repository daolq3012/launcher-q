package seoft.co.kr.launcherq.ui.setting

class BgWidgetContent {

    val widgetClassContents = arrayOf(
        WidgetClassContent("시간 형식", "시간 : HH\n분 : mm\nex) HH:mm -> 12:34"),
        WidgetClassContent("오전/오후 형식", "오전%%오후\nex) am%%pm -> 오전일시 -> am"),
        WidgetClassContent("날짜 형식", "년 : yyyy\n월 : MM\n일 : dd\nex) MM, dd -> 11, 10"),
        WidgetClassContent("요일 형식", "일%%월%%화%%수%%목%%금%%토\nex) -> 화요일 경우 -> 화"),
        WidgetClassContent("텍스트 형식", "텍스트A%%텍스트B%%...\nex) %%로 나눠진값 중 랜덤 택1 출력")

    )

    data class WidgetClassContent(val title:String, val explain:String)

}