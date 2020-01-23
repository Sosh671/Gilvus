package com.soshdev.gilvus.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    val id: Int,

    val name: String,
    val imgUrl: String
)