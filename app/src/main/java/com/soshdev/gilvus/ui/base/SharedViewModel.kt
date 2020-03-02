package com.soshdev.gilvus.ui.base

class SharedViewModel : BaseViewModel() {

    fun reconnect() {
        networkRepository.openConnection()
    }
}