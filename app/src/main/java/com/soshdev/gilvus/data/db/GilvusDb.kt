package com.soshdev.gilvus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soshdev.gilvus.data.db.dao.MessageDAO
import com.soshdev.gilvus.data.db.dao.RoomDAO
import com.soshdev.gilvus.data.db.dao.UserDAO
import com.soshdev.gilvus.data.db.models.Member
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.data.db.models.User

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

    abstract fun usersDao(): UserDAO

    abstract fun roomsDao(): RoomDAO

    abstract fun messagesDao(): MessageDAO
}