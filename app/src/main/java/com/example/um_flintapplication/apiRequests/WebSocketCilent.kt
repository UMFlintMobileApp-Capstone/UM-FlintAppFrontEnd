package com.example.um_flintapplication.apiRequests

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient(context: Context){
    private lateinit var webSocket: WebSocket
    private var socketListener: SocketListener? = null
    private var socketUrl = ""
    private var shouldReconnect = true
    private var client: OkHttpClient? = null
    private var ctx: Context = context

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: WebSocketClient
        @JvmStatic
        @Synchronized

        fun getInstance(ctx: Context): WebSocketClient {
            synchronized(WebSocketClient::class) {
                if (!::instance.isInitialized) {
                    instance = WebSocketClient(ctx)
                }
            }

            return instance
        }
    }

    fun setListener(listener: SocketListener) {
        this.socketListener = listener
    }

    fun setSocketUrl(socketUrl: String) {
        this.socketUrl = socketUrl
    }

    private fun initWebSocket() {
        Log.e("webSocket", "Initializing socket: $socketUrl")

        client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(ctx))
            .build()

        val request = Request.Builder().url(url = socketUrl).build()
        webSocket = client!!.newWebSocket(request, webSocketListener)

        client!!.dispatcher.executorService.shutdown()
    }

    fun connect() {
        Log.i("webSocket", "connecting to socket: $socketUrl")
        shouldReconnect = true
        initWebSocket()
    }

    fun reconnect() {
        Log.i("webSocket", "reconnecting to socket: $socketUrl")
        initWebSocket()
    }

    fun sendMessage(message: String) {
        Log.d("webSocket", "trying to send message via socket: $message)")
        if (::webSocket.isInitialized) webSocket.send(message)
    }

    fun disconnect() {
        if (::webSocket.isInitialized) webSocket.close(1000, "Connection is closed")
        shouldReconnect = false
    }

    interface SocketListener {
        fun onMessage(message: String)
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("webSocket", "opened socket")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            socketListener?.onMessage(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("webSocket", "closing socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("webSocket", "socket has been closed")
            if (shouldReconnect) reconnect()
        }

        override fun onFailure(
            webSocket: WebSocket, t: Throwable, response: Response?
        ) {
            Log.e("webSocket", "There is an failure on the socket: ${t.message}")
            if (shouldReconnect) reconnect()
        }
    }
}