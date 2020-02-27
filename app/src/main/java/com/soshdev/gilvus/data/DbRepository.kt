package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.db.GilvusDb
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.util.androidSubscribe
import io.reactivex.Single

class DbRepository(private val db: GilvusDb) {

    fun getRooms() = db.roomsDao().getRooms().androidSubscribe()

    fun getMessages(roomId: Long): Single<List<Message>> {
        return db.messagesDao().getMessages(roomId).androidSubscribe()
    }
}

