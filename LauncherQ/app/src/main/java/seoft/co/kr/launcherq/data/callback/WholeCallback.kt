package seoft.co.kr.launcherq.data.callback

import seoft.co.kr.launcherq.data.model.ThemeDoc
import java.io.File

interface ThemeDocCallback{
    fun onThemeDocLoad(doc: ThemeDoc)
    fun onDataNotAvailable()
}

interface FileCallback{
    fun onFileLoad(file: File)
    fun onDataNotAvailable()
}
