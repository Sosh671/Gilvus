package com.soshdev.gilvus.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soshdev.gilvus.data.models.User
import com.soshdev.gilvus.db.dao.ChatDAO

@Database(
    entities = [
        User::class
    ],
    version = 1
)
abstract class GilvusDb : RoomDatabase() {

    abstract fun chatDao(): ChatDAO
}