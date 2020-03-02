package com.soshdev.gilvus.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soshdev.gilvus.data.network.NetworkRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val networkConnection = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val errors = MutableLiveData<String>()

    protected val disposables = CompositeDisposable()
    protected val networkRepository: NetworkRepositoryImpl by inject()

    private val _serverConnectionLost = MutableLiveData<Boolean>()
    val serverConnectionLost: LiveData<Boolean> = _serverConnectionLost

    init {
        disposables += networkRepository.socketExceptionSubject.subscribe {
            _serverConnectionLost.postValue(it)
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun showLoading() {
        loadingState.value = true
//        errors.value = null
    }

    protected fun hideLoading() {
        loadingState.value = false
    }

    fun clearErrors() {
        _serverConnectionLost.value = false
    }

    protected fun onError(e: Throwable) {
        Timber.e(e)
        hideLoading()
//        if (e is CommonError) errors.value = e
    }


}