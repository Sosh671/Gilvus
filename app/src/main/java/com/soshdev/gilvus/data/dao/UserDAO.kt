package com.soshdev.gilvus.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.soshdev.gilvus.data.models.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Insert
    fun insertUser(user: User)
}