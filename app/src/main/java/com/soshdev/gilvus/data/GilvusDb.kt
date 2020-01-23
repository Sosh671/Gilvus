package com.soshdev.gilvus.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soshdev.gilvus.data.dao.ChatDAO
import com.soshdev.gilvus.data.models.Member
import com.soshdev.gilvus.data.models.Message
import com.soshdev.gilvus.data.models.Room
import com.soshdev.gilvus.data.models.User

@Database(
    entities = [
        User::class,
        Message::class,
        Room::class,
        Member::class
    ],
    version = 1
)
abstract class GilvusDb : RoomDatabase() {

    abstract fun chatDao(): ChatDAO
}