package com.soshdev.gilvus.ui.base

import com.soshdev.gilvus.util.Constants

class SharedViewModel : BaseViewModel() {

    private var host = Constants.emulatorLocalHost

    fun toggleHostAndReconnect() {
        host = if (host == Constants.emulatorLocalHost)
            Constants.baseUrl
        else
            Constants.emulatorLocalHost
        reconnect()
    }

    fun reconnect() {
        networkRepository.openConnection(host)
    }
}