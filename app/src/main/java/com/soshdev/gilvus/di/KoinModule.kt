package com.soshdev.gilvus.di

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.soshdev.gilvus.data.MockedNetworkRepositoryImpl
import com.soshdev.gilvus.data.NetworkRepository
import com.soshdev.gilvus.db.GilvusDb
import com.soshdev.gilvus.ui.chatlist.ChatListViewModel
import org.koin.dsl.module

val appModule = module {

    single<NetworkRepository> { MockedNetworkRepositoryImpl() }
    single { Room.databaseBuilder(get(), GilvusDb::class.java, "Gilvus_db")
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val cv = ContentValues()
                for (i in 1..3) {
                    cv.put("id", i)
                    cv.put("name", "User $i")
                    cv.put("imgUrl", "")
                    db.insert("users", SQLiteDatabase.CONFLICT_IGNORE, cv)
                }
            }
        })
        .build()
    }

    factory { ChatListViewModel(get(), get()) }
}