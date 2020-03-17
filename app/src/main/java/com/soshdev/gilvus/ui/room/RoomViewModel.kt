package com.soshdev.gilvus.ui.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.launchOnIO
import com.soshdev.gilvus.util.toArrayList
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class RoomViewModel(private val dbRepository: DbRepository) : BaseViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    init {
        disposables += networkRepository.getMessagesSubject
            .subscribeBy(
                onNext = {
                    if (it.status)
                        it.data?.let { m ->
                            _messages.postValue(m.toArrayList())
                        }

                    // todo else
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
        disposables += networkRepository.newMessageSubject
            .subscribeBy(
                onNext = {
                    if (it.status) {
                        val array = _messages.value as? ArrayList<Message>?: ArrayList()
                        it.data?.let { message ->  array.add(message) }
                        _messages.value = array
                    }

                    // todo else
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
    }

    fun sendMessage(token: String, roomId: Long, message: String) {
        viewModelScope.launchOnIO {
            networkRepository.sendMessage(token, roomId, message)
        }
    }

    fun getMessages(token: String, roomId: Long) {
        disposables += dbRepository.getMessages(roomId).subscribeBy(
            onSuccess = {
                _messages.value = it
                fetchMessagesFromNetwork(token, roomId)
            },
            onError = {
                fetchMessagesFromNetwork(token, roomId)
                Timber.e(it)
            }
        )
    }

    private fun fetchMessagesFromNetwork(token: String, roomId: Long) {
        viewModelScope.launchOnIO {
            networkRepository.getMessages(token, roomId)
        }
    }
}