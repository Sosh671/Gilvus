package com.soshdev.gilvus.ui.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.NetworkRepository
import com.soshdev.gilvus.data.models.Room
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.toArrayList
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class RoomsViewModel(
    private val repository: NetworkRepository,
    private val dbRepository: DbRepository
) : BaseViewModel() {

    private val _rooms = MutableLiveData<ArrayList<Room>>()
    val rooms: LiveData<ArrayList<Room>> = _rooms

    fun getRooms() {
        disposables += dbRepository.getRooms()
            .subscribeBy(
                onSuccess = {
                    _rooms.value = it.toArrayList()
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
    }
}