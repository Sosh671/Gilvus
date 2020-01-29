package com.soshdev.gilvus.data

import com.soshdev.gilvus.util.androidSubscribe

class DbRepository(private val db: GilvusDb) {

    fun getUsers() = db.usersDao().getUsers()

    fun getRooms() = db.roomsDao().getRooms().androidSubscribe()
}