package com.soshdev.gilvus.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soshdev.gilvus.data.models.BaseResponse
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

class NetworkRepositoryImpl(private val scope: CoroutineScope? = null) : Thread() {

    private val timeoutSize = 2000

    private var socket: Socket? = null
    private var outStream: OutputStream? = null
    private var inStream: InputStream? = null
    private var writer: Writer? = null

    val loginSubject: PublishSubject<BaseResponse<Nothing>> = PublishSubject.create()

    init {
        openConnection()
    }

    private fun openConnection() {
        try {
            socket = Socket()
            socket!!.connect(
                InetSocketAddress(Constants.emulatorAddress, Constants.port),
                timeoutSize
            )
            outStream = socket!!.getOutputStream()
            inStream = socket!!.getInputStream()
            Timber.d("socket $socket")


            val gson = Gson()
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Timber.e(exception)
            }
            GlobalScope.launch(exceptionHandler) {
                withContext(Dispatchers.IO) {
                    val reader = BufferedReader(InputStreamReader(inStream!!))
                    var line = reader.readLine()
                    Timber.d("received from server: $line")
                    while (line != null) {
                        val obj = JSONObject(line)
                        when (obj.getString("request")) {
                            "login" -> {
                                val type = object : TypeToken<BaseResponse<Nothing>>() {}.type
                                loginSubject.onNext(gson.fromJson(line, type))
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
        writer = null
    }

    suspend fun login(
        phone: String,
        password: String? = null
    ): JSONObject? {
        formRequestObject("login", JSONObject().apply {
            put("phone", phone)
            password?.let { put("password", password) }
        })

        return null
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

    private inner class Writer(private val outStream: OutputStream) {

        fun formRequestObject(request: String, data: JSONObject) {
            val obj = JSONObject()
            obj.put("request", request)
            obj.put("data", data)

            sendToServer(obj)
        }

        fun sendToServer(data: JSONObject) {
            println("requested $data")
            outStream?.write("$data\n".toByteArray())
        }
    }
}