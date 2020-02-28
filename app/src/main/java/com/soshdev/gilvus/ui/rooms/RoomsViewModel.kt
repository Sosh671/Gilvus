package com.soshdev.gilvus.ui.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.launchOnIO
import com.soshdev.gilvus.util.toArrayList
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class RoomsViewModel(
    private val dbRepository: DbRepository
) : BaseViewModel() {

    private val _rooms = MutableLiveData<ArrayList<Room>>()
    val rooms: LiveData<ArrayList<Room>> = _rooms

    init {
        disposables += networkRepository.getRoomsSubject
            .subscribeBy(
                onNext = {
                    if (it.status)
                        it.data?.let { rooms ->
                            _rooms.postValue(rooms.toArrayList())
                            // todo save to db
                        }

                    // todo else
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
    }

    fun getRooms(token: String) {
        disposables += dbRepository.getRooms()
            .subscribeBy(
                onSuccess = {
                    _rooms.value = it.toArrayList()
                    fetchRoomsFromNetwork(token)
                },
                onError = {
                    fetchRoomsFromNetwork(token)
                    Timber.e(it)
                    //todo on error
                }
            )
    }

    private fun fetchRoomsFromNetwork(token: String) {
        viewModelScope.launchOnIO {
            networkRepository.getRooms(token)
        }
    }
}