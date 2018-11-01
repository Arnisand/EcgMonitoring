package com.arnisand.ecgmonitoring.ui.main.monitor

import android.content.Intent
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper
import com.arnisand.ecgmonitoring.ui.base.BasePresenter
import javax.inject.Inject

class MonitoringPresenter @Inject constructor() : BasePresenter<IMonitoringFragment>(), IMonitoringPresenter {

    override fun handleMessage(intent: Intent) {
        intent.takeIf { it.hasExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE) }?.let {
            when (it.getIntExtra(EcgApiSocketHelper.SOCKET_MESSAGE_TYPE, 0)) {
                EcgApiSocketHelper.SocketType.MESSAGE.index -> {
                    getView()?.newDataEcg(it.getStringExtra(EcgApiSocketHelper.SOCKET_MESSAGE))
                }
                EcgApiSocketHelper.SocketType.OPEN.index -> {
                    getView()?.statusConnected(true)
                }
                EcgApiSocketHelper.SocketType.CLOSE.index -> {
                    getView()?.statusConnected(false)
                }
                EcgApiSocketHelper.SocketType.FAILURE.index -> {
                    getView()?.statusConnected(false)
                }
                else -> {
                }
            }
        }
    }
}