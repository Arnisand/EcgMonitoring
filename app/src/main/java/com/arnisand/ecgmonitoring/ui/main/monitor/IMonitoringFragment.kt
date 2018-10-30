package com.arnisand.ecgmonitoring.ui.main.monitor

import com.arnisand.ecgmonitoring.ui.base.MvpView

interface IMonitoringFragment: MvpView {

    fun statusConnected(status: Boolean)

    fun newDataEcg(message: String)
}