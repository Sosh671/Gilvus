package com.soshdev.gilvus.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soshdev.gilvus.data.network.NetworkRepositoryImpl
import com.soshdev.gilvus.util.PrefsHelper
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val networkConnection = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val errors = MutableLiveData<String>()

    protected val disposables = CompositeDisposable()
    protected val networkRepository: NetworkRepositoryImpl by inject()
    protected val prefsHelper: PrefsHelper by inject()

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
//        errors.value = null
    }

    protected fun onError(e: Throwable) {
        Timber.e(e)
        hideLoading()
//        if (e is CommonError) errors.value = e
    }


}