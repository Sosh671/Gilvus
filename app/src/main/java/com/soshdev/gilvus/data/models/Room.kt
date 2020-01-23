package com.soshdev.gilvus.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class Room(

    @PrimaryKey
    val id: Long,

    val name: String,
    val creationalDate: Long
)
