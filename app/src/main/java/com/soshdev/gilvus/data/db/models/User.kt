package com.soshdev.gilvus.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    var id: Long?,

    val name: String,
    val phone: String?,
    val avatarUrl: String?,
    var registered: Boolean
)