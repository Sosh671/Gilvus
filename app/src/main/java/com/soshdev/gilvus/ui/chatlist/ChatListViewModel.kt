package com.soshdev.gilvus.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soshdev.gilvus.data.DataRepository
import com.soshdev.gilvus.data.models.Chat

class ChatListViewModel(private val repository: DataRepository) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    fun getChats() {
        _chats.value = repository.getChatList()
    }
}