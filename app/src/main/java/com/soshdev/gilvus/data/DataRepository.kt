package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.models.Chat

interface DataRepository {

    fun getChatList(): List<Chat>
}