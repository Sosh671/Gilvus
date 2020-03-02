package com.soshdev.gilvus.data.network

import com.soshdev.gilvus.data.db.models.User

class MockedNetworkRepositoryImpl : NetworkRepository {

    override fun getUsersList(): List<User> {
        val list = mutableListOf<User>()
        for (i in 1..10L)
            list.add(User(i, "User $i", "+3234234234234", "", false))
        return list
    }
}