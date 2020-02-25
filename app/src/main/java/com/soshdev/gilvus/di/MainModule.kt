package com.soshdev.gilvus.di

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.db.GilvusDb
import com.soshdev.gilvus.data.network.MockedNetworkRepositoryImpl
import com.soshdev.gilvus.data.network.NetworkRepository
import com.soshdev.gilvus.ui.authorization.AuthorizationViewModel
import com.soshdev.gilvus.ui.chat.ChatViewModel
import com.soshdev.gilvus.ui.confirm.ConfirmViewModel
import com.soshdev.gilvus.ui.newroom.NewRoomViewModel
import com.soshdev.gilvus.ui.profile.ProfileViewModel
import com.soshdev.gilvus.ui.rooms.RoomsViewModel
import com.soshdev.gilvus.util.PrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<NetworkRepository> { MockedNetworkRepositoryImpl() }
    single {
        Room.databaseBuilder(get(), GilvusDb::class.java, "Gilvus_db")
           /* .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    populateDbWithMockedData(db)
                }
            })*/
            .build()
    }

    factory { DbRepository(get()) }
    factory { RoomsViewModel(get(), get()) }
    factory { ChatViewModel(get()) }
    factory { ProfileViewModel() }
    factory { NewRoomViewModel(get()) }
    factory { AuthorizationViewModel() }
    factory { ConfirmViewModel() }
    single { PrefsHelper(androidContext()) }
}

fun populateDbWithMockedData(db: SupportSQLiteDatabase) {

    // users
    var cv = ContentValues()
    for (i in 1..3) {
        cv.put("name", "User $i")
        cv.put("avatarUrl", "")
        db.insert("users", SQLiteDatabase.CONFLICT_IGNORE, cv)
    }

    // rooms
    cv = ContentValues()
    for (i in 1..4) {
        cv.put("name", "Room $i")
        cv.put("creationalDate", System.currentTimeMillis())
        val id = db.insert("rooms", SQLiteDatabase.CONFLICT_IGNORE, cv)
    }

    val roomMembers = ArrayList<TestRoomMember>()
    roomMembers.add(TestRoomMember(1, ArrayList()))
    roomMembers[0].users.add(1)
    roomMembers[0].users.add(2)
    roomMembers.add(TestRoomMember(2, ArrayList()))
    roomMembers[1].users.add(2)
    roomMembers[1].users.add(3)
    roomMembers.add(TestRoomMember(3, ArrayList()))
    roomMembers[2].users.add(1)
    roomMembers[2].users.add(3)
    roomMembers[2].users.add(2)
    roomMembers.add(TestRoomMember(4, ArrayList()))
    roomMembers[3].users.add(1)
    roomMembers[3].users.add(3)

    // members
    for (i in 0 until roomMembers.size) {
        for (j in 0 until roomMembers[i].users.size) {
            db.putMember(roomMembers[i].roomId, roomMembers[i].users[j])
        }
    }

    val mockedMessage =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum nec pellentesque nisl, in aliquam libero. Pellentesque enim est, dictum non sapien non, ornare pulvinar sapien. Suspendisse rhoncus diam quis quam fermentum egestas. Etiam lectus nunc, suscipit nec pharetra at, luctus eget lectus. "

    // messages
    cv = ContentValues()
    for(i in 0 until roomMembers.size) {
        val room = roomMembers[i]
        val messageCount = (3..15).random()
        for (j in 1..messageCount) {
            val message = mockedMessage.substring(0, (10 until mockedMessage.length).random())
            val messageOwner = room.users.random()
            cv.put("roomId", room.roomId)
            cv.put("userId", messageOwner)
            cv.put("date", System.currentTimeMillis())
            cv.put("text", message)
            db.insert("messages", SQLiteDatabase.CONFLICT_IGNORE, cv)
        }
    }
}

fun SupportSQLiteDatabase.putMember(roomId: Long, userId: Long) {
    val cv = ContentValues()
    cv.put("roomId", roomId)
    cv.put("userId", userId)
    this.insert("members", SQLiteDatabase.CONFLICT_IGNORE, cv)
}

data class TestRoomMember(
    val roomId: Long,
    val users: ArrayList<Long>
)