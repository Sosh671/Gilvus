package com.soshdev.gilvus.ui.base

import com.soshdev.gilvus.util.Constants

class SharedViewModel : BaseViewModel() {

    var host = Constants.baseUrl
        private set

    /*fun toggleHostAndReconnect() {
        host = if (host == Constants.emulatorLocalHost)
            Constants.baseUrl
        else
            Constants.emulatorLocalHost
        reconnect()
    }*/

    fun reconnect() {
        networkRepository.openConnection(host)
    }
}