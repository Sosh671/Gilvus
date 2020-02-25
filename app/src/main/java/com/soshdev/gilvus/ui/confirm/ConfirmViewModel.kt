package com.soshdev.gilvus.ui.confirm

import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.PrefsHelper
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ConfirmViewModel : BaseViewModel() {

    init {
        disposables += repositoryImpl.confirmLoginSubject.subscribeBy(
            onNext = {
                Timber.d("$it")
                if (it.status) {
                    getKoin().get<PrefsHelper>().apply { putToken(it.data!!.token) }
                }
                //todo else
            },
            // todo error
            onError = Timber::e
        )
    }

    fun confirm(phone: String, code: Int) {
        viewModelScope.launchOnIO {
            repositoryImpl.confirmLogin(phone, code)
        }
    }
}