package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.models.Chat

class MockedDataRepositoryImpl: DataRepository {
    override fun getChatList(): List<Chat> {
        val list = mutableListOf<Chat>()
        for (i in 1..10)
            list.add(Chat(i, "User $i", ""))
        return list
    }
}