package com.soshdev.gilvus.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.models.Message
import com.soshdev.gilvus.ui.base.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ChatViewModel(private val dbRepository: DbRepository): BaseViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    fun getMessages(roomId: Long) {
        disposables += dbRepository.getMessages(roomId).subscribeBy(
            onSuccess = {
                _messages.value = it
                for (message in it)
                    Timber.d("message ${message.id} sent by me ${message.sentByCurrentUser}")
            },
            onError = {
                Timber.e(it)
                // todo on error
            }
        )
    }
}