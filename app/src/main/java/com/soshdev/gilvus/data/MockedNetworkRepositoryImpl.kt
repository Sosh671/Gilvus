package com.soshdev.gilvus.data

import com.soshdev.gilvus.data.models.User

class MockedNetworkRepositoryImpl: NetworkRepository {

    override fun getUsersList(): List<User> {
        val list = mutableListOf<User>()
        for (i in 1..10)
            list.add(User(i, "User $i", ""))
        return list
    }
}