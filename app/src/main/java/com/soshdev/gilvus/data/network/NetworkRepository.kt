package com.soshdev.gilvus.data.network

import com.soshdev.gilvus.data.db.models.User

interface NetworkRepository {

    fun getUsersList(): List<User>
}