package com.soshdev.gilvus.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.AuthorizationState
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class AuthorizationViewModel : BaseViewModel() {

    private val _testCode = MutableLiveData<Int>()
    val testCode = _testCode as LiveData<Int>

    var phone = ""
    var state = AuthorizationState.LOGIN

    init {
        disposables += repositoryImpl.loginSubject.subscribeBy(
            onNext = {
                if (it.status)
                    _testCode.postValue(it.data!!.code)
                // todo else
                else
                    errors.postValue(it.errorMessage)
            },
            //todo error
            onError = Timber::d
        )
        disposables += repositoryImpl.registrationSubject.subscribeBy(
            onNext = {
                if (it.status)
                    _testCode.postValue(it.data!!.code)
                // todo else
                else
                    errors.postValue(it.errorMessage)
            },
            //todo error
            onError = Timber::d
        )
    }

    fun testClear() {
        _testCode.value = 0
    }

    fun login(phone: String, password: String?) {
        viewModelScope.launchOnIO {
            repositoryImpl.login(phone, password)
        }
    }

    fun registration(phone: String, password: String?) {
        viewModelScope.launchOnIO {
            repositoryImpl.registration(phone, password)
        }
    }
}