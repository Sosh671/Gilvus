package com.soshdev.gilvus.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    val id: Int?,

    val name: String,
    val phone: String?,
    val avatarUrl: String?
)