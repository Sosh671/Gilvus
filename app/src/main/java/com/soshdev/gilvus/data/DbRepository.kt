package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.models.Message
import com.soshdev.gilvus.util.CurrentUserCompanion
import com.soshdev.gilvus.util.androidSubscribe
import io.reactivex.Single

class DbRepository(private val db: GilvusDb) {

    fun getUsers() = db.usersDao().getUsers()

    fun getRooms() = db.roomsDao().getRooms().androidSubscribe()

    fun getMessages(roomId: Long): Single<List<Message>> {
        val single = db.messagesDao().getMessages(roomId).androidSubscribe().map {
            it.forEach { message ->
                message.sentByCurrentUser = message.userId == CurrentUserCompanion.currentUserId
            }
            it
        }
      /*  single.map {
            it.forEach { message ->
                message.sentByCurrentUser = message.userId == CurrentUserCompanion.currentUserId
            }
        }*/
        return single
    }


}

