package com.soshdev.gilvus.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val networkConnection = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
//    val errors = MutableLiveData<CommonError?>()

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun Disposable.untilDestroy() {
        disposables.add(this)
    }

    protected fun showLoading() {
        loadingState.value = true
//        errors.value = null
    }

    protected fun hideLoading() {
        loadingState.value = false
    }

    fun clearErrors(){
//        errors.value = null
    }

    protected fun onError(e: Throwable) {
        Timber.e(e)
        hideLoading()
//        if (e is CommonError) errors.value = e
    }
}