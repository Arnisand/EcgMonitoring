package com.arnisand.ecgmonitoring.background

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.arnisand.ecgmonitoring.data.DataManager
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper
import com.arnisand.ecgmonitoring.di.component.DaggerServiceComponent
import com.arnisand.ecgmonitoring.ui.EcgApplication
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class SocketService : Service() {

    @Inject
    lateinit var mDataManager: DataManager

    private val ecgReceiver = EcgReceiver(this)

    private val mHandler = Handler()
    private var mTimer: Runnable? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        DaggerServiceComponent.builder()
                .applicationComponent(EcgApplication.applicationComponent)
                .build()
                .inject(this)
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(ecgReceiver, IntentFilter(EcgApiSocketHelper.SOCKET_BROADCAST_ACTION))
        super.onCreate()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(ecgReceiver)
        mDataManager.closeSocketConnection()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mDataManager.openSocketConnection()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleMessage(intent: Intent) {
        intent.takeIf { it.hasExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE) }?.let {
            when (it.getIntExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE, 0)) {
                EcgApiSocketHelper.SocketType.OPEN.index -> {
                    stopGenerateSignal()
                }
                EcgApiSocketHelper.SocketType.CLOSE.index -> {
                    startGenerateSignal()
                    mDataManager.openSocketConnection()
                }
                else -> {
                }
            }
        }
    }

    private fun startGenerateSignal() {
        mTimer = object : Runnable {
            override fun run() {
                sendBroadcast((Random().nextFloat()).toString())
                mHandler.postDelayed(this, 300)
            }
        }
        mHandler.postDelayed(mTimer, 300)
    }

    private fun stopGenerateSignal() {
        mHandler.removeCallbacks(mTimer)
    }

    private fun sendBroadcast(text: String) {
        val intent = Intent().apply { action = EcgApiSocketHelper.SOCKET_BROADCAST_ACTION }
        intent.putExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE, EcgApiSocketHelper.SocketType.MESSAGE.index)
        intent.putExtra(EcgApiSocketHelper.SOCKET_MESSAGE, text)
        LocalBroadcastManager.getInstance(EcgApplication.instance).sendBroadcast(intent)
    }

    class EcgReceiver(socketService: SocketService) : BroadcastReceiver() {
        private val wMonitoringFragment = WeakReference(socketService)

        override fun onReceive(context: Context?, intent: Intent) {
            wMonitoringFragment.get()?.handleMessage(intent)
        }
    }
}