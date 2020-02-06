package com.soshdev.gilvus.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.soshdev.gilvus.data.db.models.Room
import io.reactivex.Single

@Dao
abstract class RoomDAO {

    @Query("SELECT * FROM rooms")
    abstract fun getRooms(): Single<List<Room>>
}