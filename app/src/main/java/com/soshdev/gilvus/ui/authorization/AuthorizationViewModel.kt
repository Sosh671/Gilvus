package com.soshdev.gilvus.ui.authorization

import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthorizationViewModel : BaseViewModel() {

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {

            repositoryImpl.loginSubject.subscribe {
                Timber.d("login subject $it")
            }
            repositoryImpl.login("12")
        }
    }
}