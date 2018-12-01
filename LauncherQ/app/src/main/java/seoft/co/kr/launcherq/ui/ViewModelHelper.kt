package seoft.co.kr.launcherq.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import seoft.co.kr.launcherq.utill.i

abstract class ViewModelHelper : ViewModel() {

    var actMsg = MutableLiveData<MsgType>()
    var msg:Any? = null
    val isLoading = ObservableField(false)

    abstract fun start()

    fun toActMsg(type:MsgType,msg_:Any? = null, showLog:Boolean = false) {
        if(showLog) "SEND TO ACTVITY :: type -> ${type.name}    msg -> ${msg_.toString()}".i()
        msg = msg_
        actMsg.value = type
    }

    fun create(): ViewModelFactory {
        return ViewModelFactory(this)
    }
}

class ViewModelFactory(val vm: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = vm as T
}