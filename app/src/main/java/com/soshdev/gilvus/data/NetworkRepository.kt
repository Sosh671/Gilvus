package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.models.User

interface NetworkRepository {

    fun getUsersList(): List<User>
}