package com.soshdev.gilvus.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class AuthorizationViewModel : BaseViewModel() {

    private val _testCode = MutableLiveData<Int>()
    val testCode = _testCode as LiveData<Int>

    init {
        disposables += repositoryImpl.loginSubject.subscribeBy(
            onNext = {
                Timber.d("login subject $it")
                if (it.status)
                    _testCode.postValue(it.data!!.code)
                else
                    errors.postValue(it.errorMessage)
            },
            //todo error
            onError = Timber::d
        )
    }

    fun login(phone: String) {
        viewModelScope.launchOnIO {
            repositoryImpl.login(phone)
        }
    }
}