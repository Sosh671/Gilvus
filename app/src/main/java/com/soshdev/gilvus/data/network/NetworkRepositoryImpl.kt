package com.soshdev.gilvus.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soshdev.gilvus.data.models.BaseResponse
import com.soshdev.gilvus.data.models.TestCode
import com.soshdev.gilvus.data.models.Token
import com.soshdev.gilvus.util.Constants
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class NetworkRepositoryImpl {

    private val timeoutSize = 2000

    private var socket: Socket? = null
    private var outStream: OutputStream? = null
    private var inStream: InputStream? = null

    val loginSubject: PublishSubject<BaseResponse<TestCode>> = PublishSubject.create()
    val confirmLoginSubject: PublishSubject<BaseResponse<Token>> = PublishSubject.create()

    init {
        openConnection()
    }

    fun openConnection() {
        try {
            val gson = Gson()
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Timber.e(exception)
            }
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
                    Timber.d("received from server: $line")
                    while (line != null) {
                        val obj = JSONObject(line)
                        when (obj.getString("request")) {
                            "registration" -> {
                            }
                            "login" -> {
                                val type = object : TypeToken<BaseResponse<TestCode>>() {}.type
                                loginSubject.onNext(gson.fromJson(line, type))
                            }
                            "confirm_login" -> {
                                val type = object : TypeToken<BaseResponse<Token>>() {}.type
                                confirmLoginSubject.onNext(gson.fromJson(line, type))
                            }
                            "confirm_registration" -> {
                            }
                            "get_rooms" -> {
                            }
                            "add_room" -> {
                            }
                            "get_messages" -> {
                            }
                            "send_message" -> {
                            }
                        }

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

    suspend fun formRequestObject(request: String, data: JSONObject) {
        val obj = JSONObject()
        obj.put("request", request)
        obj.put("data", data)

        sendToServer(obj)
    }

    suspend fun sendToServer(data: JSONObject) {
        println("requested $data")
        withContext(Dispatchers.IO) {
            outStream?.write("$data\n".toByteArray())
        }
    }
}