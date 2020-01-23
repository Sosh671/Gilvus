package com.soshdev.gilvus.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.GilvusDb
import com.soshdev.gilvus.data.NetworkRepository
import com.soshdev.gilvus.data.models.User
import com.soshdev.gilvus.ui.base.BaseViewModel

class ChatListViewModel(
    private val repository: NetworkRepository,
    private val db: GilvusDb
    ) : BaseViewModel() {

    private val _chats = MutableLiveData<List<User>>()
    val chats: LiveData<List<User>> = _chats

    fun getChats() {
        viewModelScope.launchOnDefault {
            _chats.postValue(db.chatDao().getUsers())
        }
    }
}