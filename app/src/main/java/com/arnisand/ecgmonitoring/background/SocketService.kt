package com.arnisand.ecgmonitoring.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper.Companion.TOKEN
import com.arnisand.ecgmonitoring.ui.EcgApplication

class SocketService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        EcgApplication.mDataManager?.openSocketConnection(TOKEN)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        EcgApplication.mDataManager?.closeSocketConnection()
        super.onDestroy()
    }
}