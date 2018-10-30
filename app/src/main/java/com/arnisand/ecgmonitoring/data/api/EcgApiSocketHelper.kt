package com.arnisand.ecgmonitoring.data.api

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.arnisand.ecgmonitoring.ui.EcgApplication
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

class EcgApiSocketHelper @Inject constructor(): ApiSocketHelper {

    private var socketClient: WebSocketClient? = null

    override fun openSocketConnection(token: String) {
        socketClient = SocketClient(URI(CONNECTION_URL))
        socketClient?.connect()
    }

    override fun sendMessage(text: String) {
        if (socketClient?.isOpen == true) {
            try {
                socketClient?.send(text)
            } catch (t: Throwable) {
                Log.e(TAG, Log.getStackTraceString(t))
            }
        }
    }

    override fun closeSocketConnection() {
        socketClient?.close(CLOSE_REASON_OK)
    }

    class SocketClient(uri: URI) : WebSocketClient(uri) {
        private val mPowerManager by lazy { EcgApplication.instance?.getSystemService(Context.POWER_SERVICE) as PowerManager }
        @Suppress("DEPRECATION")
        private var mSocketWakeLock: PowerManager.WakeLock? = null

        init {
            connectionLostTimeout = PING_TIMEOUT
            Log.e("message", "init $this ${Thread.currentThread()}")
        }

        override fun onOpen(handshakedata: ServerHandshake?) {
            sendBroadcast(SocketType.OPEN.index)
            Log.e("message", "open $this ${Thread.currentThread()}")
        }

        override fun onMessage(message: String?) {
            sendBroadcast(SocketType.MESSAGE.index, message)
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            if (code != CLOSE_REASON_OK) {
                sendBroadcast(SocketType.CLOSE.index)
            }
            mSocketWakeLock?.takeIf { it.isHeld }?.run { release() }
            Log.e("message", "close $this ${Thread.currentThread()} $this with $code and $reason")
        }

        override fun onError(ex: Exception?) {
            sendBroadcast(SocketType.FAILURE.index)
            Log.e("socket error", Log.getStackTraceString(ex))
        }

        private fun sendBroadcast(type: Int) {
            val intent = Intent().apply { action = SOCKET_BROADCAST_ACTION }
            intent.putExtra(SOCKET_MESSAGE_TYPE, type)
            EcgApplication.instance?.let {
                LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
            }
        }

        private fun sendBroadcast(type: Int, text: String?) {
            val intent = Intent().apply { action = SOCKET_BROADCAST_ACTION }
            intent.putExtra(SOCKET_MESSAGE_TYPE, type)
            intent.putExtra(SOCKET_MESSAGE, text)
            EcgApplication.instance?.let {
                LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
            }
        }
    }

    enum class SocketType(val index: Int) {
        OPEN(100),
        CLOSE(101),
        MESSAGE(102),
        FAILURE(103)
    }

    companion object {
        val TAG = EcgApiSocketHelper::class.qualifiedName

        const val TOKEN = "TOKEN"
        const val SOCKET_BROADCAST_ACTION = "ecgmonitoring.socket"
        const val SOCKET_MESSAGE = "socket message"
        const val SOCKET_MESSAGE_TYPE = "socket message type"
        const val PING_TIMEOUT = 15
        const val WAKE_TIMEOUT = 5000L
        const val RECONNECT_TIME = 5000
        const val CONNECTION_URL = "ws://192.168.1.102:10080/monitoring"
        private const val CLOSE_REASON_OK = 132
    }
}