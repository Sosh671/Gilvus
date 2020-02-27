package com.soshdev.gilvus.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.data.models.BaseResponse
import com.soshdev.gilvus.data.models.Id
import com.soshdev.gilvus.data.models.TestCode
import com.soshdev.gilvus.data.models.Token
import com.soshdev.gilvus.util.Constants
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.net.Socket

class NetworkRepositoryImpl {

    private val timeoutSize = 2000

    private var socket: Socket? = null
    private var outStream: OutputStream? = null
    private var inStream: InputStream? = null
    private val gson by lazy { Gson() }
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        // todo coroutine exception
    }

    init {
        openConnection()
    }

    fun openConnection() {
        try {
            GlobalScope.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    socket = Socket()
                    socket!!.connect(
                        InetSocketAddress(Constants.emulatorAddress, Constants.port),
                        timeoutSize
                    )
                    outStream = socket!!.getOutputStream()
                    inStream = socket!!.getInputStream()
                    Timber.d("socket $socket")

                    val reader = BufferedReader(InputStreamReader(inStream!!))
                    var line = reader.readLine()
                    while (line != null) {
                        Timber.d("received from server: $line")
                        handleServerResponse(line)

                        line = reader.readLine()
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            closeConnection()
        }
    }

    fun closeConnection() {
        inStream = null
        outStream = null
        socket = null
    }

    val loginSubject: PublishSubject<BaseResponse<TestCode>> = PublishSubject.create()
    val confirmLoginSubject: PublishSubject<BaseResponse<Token>> = PublishSubject.create()
    val registrationSubject: PublishSubject<BaseResponse<TestCode>> = PublishSubject.create()
    val confirmRegistrationSubject: PublishSubject<BaseResponse<Token>> = PublishSubject.create()
    val getRoomsSubject: PublishSubject<BaseResponse<List<Room>>> = PublishSubject.create()
    val createRoomSubject: PublishSubject<BaseResponse<Id>> = PublishSubject.create()
    val getMessagesSubject: PublishSubject<BaseResponse<List<Message>>> = PublishSubject.create()

    private val typeTokensList = TypeTokensList()

    private fun handleServerResponse(response: String) {
        try {
            val obj = JSONObject(response)
            when (obj.getString("request")) {
                "login" -> {
                    loginSubject.onNext(gson.fromJson(response, typeTokensList.typeCode))
                }
                "confirm_login" -> {
                    confirmLoginSubject.onNext(gson.fromJson(response, typeTokensList.typeToken))
                }
                "registration" -> {
                    loginSubject.onNext(gson.fromJson(response, typeTokensList.typeCode))
                }
                "confirm_registration" -> {
                    confirmLoginSubject.onNext(gson.fromJson(response, typeTokensList.typeToken))
                }
                "get_rooms" -> {
                    val old: BaseResponse<Map<String, List<Room>>> =
                        gson.fromJson(response, typeTokensList.typeRoomsList)
                    val new: BaseResponse<List<Room>> = BaseResponse(
                        old.status,
                        old.request,
                        old.errorMessage,
                        old.data?.get("rooms")
                    )
                    getRoomsSubject.onNext(new)
                }
                "add_room" -> {
//                    createRoomSubject.onNext(gson.fromJson(response, typeTokensList.typeRoom))
                }
                "get_messages" -> {
                    val old: BaseResponse<Map<String, List<Message>>> =
                        gson.fromJson(response, typeTokensList.typeMessagesList)

                    val new: BaseResponse<List<Message>> = BaseResponse(
                        old.status,
                        old.request,
                        old.errorMessage,
                        old.data?.get("messages")
                    )
                    getMessagesSubject.onNext(new)
                }
                "send_message" -> {
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun login(phone: String, password: String? = null) {
        formRequestObject("login", JSONObject().apply {
            put("phone", phone)
            password?.let { put("password", password) }
        })
    }

    suspend fun confirmLogin(phone: String, code: Int) {
        formRequestObject("confirm_login", JSONObject().apply {
            put("phone", phone)
            put("code", code)
        })
    }

    suspend fun registration(phone: String, password: String? = null) {
        formRequestObject("registration", JSONObject().apply {
            put("phone", phone)
            password?.let { put("password", password) }
        })
    }

    suspend fun confirmRegistration(phone: String, code: Int) {
        formRequestObject("confirm_registration", JSONObject().apply {
            put("phone", phone)
            put("code", code)
        })
    }

    suspend fun getRooms(token: String) {
        formRequestObject("get_rooms", JSONObject().apply {
            put("token", token)
        })
    }

    suspend fun getRoomInfo(token: String, roomId: Long) {
        formRequestObject("get_room_info", JSONObject().apply {
            put("token", token)
            put("room_id", roomId)
        })
    }

    suspend fun createRoom(token: String, title: String, members: List<Id>) {
        formRequestObject("add_room", JSONObject().apply {
            put("token", token)
            put("title", title)
            put("members", JSONArray(members))
        })
    }

    private var currentuser = 0
    suspend fun getMessages(token: String, roomId: Long) {
        formRequestObject("get_messages", JSONObject().apply {
            put("token", token)
            put("room_id", roomId)
        })
    }

    suspend fun sendMessage(token: String, roomId: Long, message: String) {
        formRequestObject("get_messages", JSONObject().apply {
            put("token", token)
            put("room_id", roomId)
            put("message", message)
        })
    }

    private suspend fun formRequestObject(request: String, data: JSONObject) {
        val obj = JSONObject()
        obj.put("request", request)
        obj.put("data", data)

        sendToServer(obj)
    }

    private suspend fun sendToServer(data: JSONObject) {
        Timber.d("requested $data")
        withContext(Dispatchers.IO) {
            outStream?.write("$data\n".toByteArray())
        }
    }

    private class TypeTokensList {
        val typeCode: Type by lazy { object : TypeToken<BaseResponse<TestCode>>() {}.type }
        val typeToken: Type by lazy { object : TypeToken<BaseResponse<Token>>() {}.type }
        val typeRoomsList: Type by lazy {
            object : TypeToken<BaseResponse<Map<String, List<Room>>>>() {}.type
        }
        val typeMessagesList: Type by lazy {
            object : TypeToken<BaseResponse<Map<String, List<Message>>>>() {}.type
        }
    }
}