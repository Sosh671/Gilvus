package com.soshdev.gilvus.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.ui.base.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            onError = Timber::d
        )
    }

    fun login(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.login(phone)
        }
    }
}