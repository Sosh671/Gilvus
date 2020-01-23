package com.soshdev.gilvus.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.soshdev.gilvus.data.models.User

@Dao
interface ChatDAO {

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Insert
    fun insertUser(user: User)
}