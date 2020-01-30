package com.soshdev.gilvus.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.soshdev.gilvus.data.models.Message
import io.reactivex.Single

@Dao
interface MessageDAO {

    @Query("SELECT * FROM messages WHERE roomId = :roomId")
    fun getMessages(roomId: Long): Single<List<Message>>
}