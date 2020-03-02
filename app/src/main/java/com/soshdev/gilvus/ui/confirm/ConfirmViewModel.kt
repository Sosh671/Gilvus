package com.soshdev.gilvus.ui.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.PrefsHelper
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ConfirmViewModel(private val prefsHelper: PrefsHelper) : BaseViewModel() {

    private val _successfulAuthorization = MutableLiveData<Boolean>()
    val successfulAuthorization: LiveData<Boolean> = _successfulAuthorization

    init {
        disposables += networkRepository.confirmLoginSubject.subscribeBy(
            onNext = {
                if (it.status) {
                    it.data?.token?.let { token ->
                        prefsHelper.putToken(token)
                        _successfulAuthorization.value = true
                    }
                }
                //todo else
            },
            // todo error
            onError = Timber::e
        )
        disposables += networkRepository.confirmRegistrationSubject.subscribeBy(
            onNext = {
                if (it.status) {
                    it.data?.token?.let { token -> prefsHelper.putToken(token) }
                }
                //todo else
            },
            // todo error
            onError = Timber::e
        )
    }

    fun confirmLogin(phone: String, code: Int) {
        viewModelScope.launchOnIO {
            networkRepository.confirmLogin(phone, code)
        }
    }

    fun confirmRegistration(phone: String, code: Int) {
        viewModelScope.launchOnIO {
            networkRepository.confirmRegistration(phone, code)
        }
    }
}