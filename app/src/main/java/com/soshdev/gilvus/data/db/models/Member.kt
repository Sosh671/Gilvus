package com.soshdev.gilvus.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "members",
    foreignKeys = [ForeignKey(
        entity = User::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId")
    ), ForeignKey(
        entity = Room::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("roomId")
    )]
)
data class Member (

    @PrimaryKey
    val id: Long,

    val roomId: Long,
    val userId: Long
)
