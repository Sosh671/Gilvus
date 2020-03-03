package com.soshdev.gilvus.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.data.models.*
import com.soshdev.gilvus.util.Constants
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.*
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.net.Socket

class NetworkRepositoryImpl {

    private val timeoutSize = 2000

    private var socket: Socket? = null
    private var outStream: OutputStream? = null
    private var inStream: InputStream? = null
    private val gson by lazy { Gson() }

    private val connectionExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        closeConnection()
        socketExceptionSubject.onNext(true)
    }
    val socketExceptionSubject: PublishSubject<Boolean> = PublishSubject.create()

    init {
        openConnection()
    }

    fun openConnection(host: String = Constants.emulatorLocalHost) {
        GlobalScope.launch(connectionExceptionHandler) {
            withContext(Dispatchers.IO) {
                socket = Socket()
                socket!!.connect(InetSocketAddress(host, Constants.port), timeoutSize)
                Timber.d("socket $socket")

                outStream = socket!!.getOutputStream()
                inStream = socket!!.getInputStream()
                val reader = BufferedReader(InputStreamReader(inStream!!))

                // send false to indicate that the connection is successful
                socketExceptionSubject.onNext(false)

                var line = reader.readLine()
                while (line != null) {
                    Timber.d("received from server: $line")
                    handleServerResponse(line)
                    line = reader.readLine()
                }
            }
        }
    }

    private fun closeConnection() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Timber.e(e)
        } finally {
            inStream = null
            outStream = null
            socket = null
        }
    }

    val loginSubject: PublishSubject<BaseResponse<TestCode>> = PublishSubject.create()
    val confirmLoginSubject: PublishSubject<BaseResponse<Token>> = PublishSubject.create()
    val registrationSubject: PublishSubject<BaseResponse<TestCode>> = PublishSubject.create()
    val confirmRegistrationSubject: PublishSubject<BaseResponse<Token>> = PublishSubject.create()
    val getRoomsSubject: PublishSubject<BaseResponse<List<Room>>> = PublishSubject.create()
    val createRoomSubject: PublishSubject<BaseResponse<Id>> = PublishSubject.create()
    val getMessagesSubject: PublishSubject<BaseResponse<List<Message>>> = PublishSubject.create()
    val checkContactsSubject: PublishSubject<BaseResponse<List<IdAndPhone>>> =
        PublishSubject.create()

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
                "check_contacts" -> {
                    val old: BaseResponse<Map<String, List<IdAndPhone>>> =
                        gson.fromJson(response, typeTokensList.idAndPhoneToken)

                    val new: BaseResponse<List<IdAndPhone>> = BaseResponse(
                        old.status,
                        old.request,
                        old.errorMessage,
                        old.data?.get("registered_numbers")
                    )
                    checkContactsSubject.onNext(new)
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

    suspend fun checkContacts(token: String, array: Array<String>) {
        formRequestObject("check_contacts", JSONObject().apply {
            put("token", token)
            put("phone_numbers",
                JSONArray().also { jArray ->
                    array.forEach { number ->
                        jArray.put(JSONObject().apply { put("number", number) })
                    }
                })
        })
    }

    private suspend fun formRequestObject(request: String, data: JSONObject) {
        val obj = JSONObject()
        obj.put("request", request)
        obj.put("data", data)

        sendToServer(obj)
    }

    private suspend fun sendToServer(data: JSONObject) {
        Timber.d("requested $data socket: $socket")
        withContext(Dispatchers.IO) {
            outStream?.write("$data\n".toByteArray())
        }
    }

    private class TypeTokensList {
        val typeCode: Type by lazy {
            object : TypeToken<BaseResponse<TestCode>>() {}.type
        }
        val typeToken: Type by lazy {
            object : TypeToken<BaseResponse<Token>>() {}.type
        }
        val typeRoomsList: Type by lazy {
            object : TypeToken<BaseResponse<Map<String, List<Room>>>>() {}.type
        }
        val typeMessagesList: Type by lazy {
            object : TypeToken<BaseResponse<Map<String, List<Message>>>>() {}.type
        }
        val idAndPhoneToken: Type by lazy {
            object : TypeToken<BaseResponse<Map<String, List<IdAndPhone>>>>() {}.type
        }
    }
}