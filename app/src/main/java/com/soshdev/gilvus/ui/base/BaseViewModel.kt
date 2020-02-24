package com.soshdev.gilvus.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soshdev.gilvus.data.network.NetworkRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val networkConnection = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    //    val errors = MutableLiveData<CommonError?>()

    val repositoryImpl: NetworkRepositoryImpl by inject()

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

    fun clearErrors() {
//        errors.value = null
    }

    protected fun onError(e: Throwable) {
        Timber.e(e)
        hideLoading()
//        if (e is CommonError) errors.value = e
    }

    fun CoroutineScope.launchOnDefault(block: () -> Unit) {
        this.launch(Dispatchers.IO) {
            block.invoke()
        }
    }
}