package com.arnisand.ecgmonitoring.ui.main.monitor

import android.content.Intent
import com.arnisand.ecgmonitoring.ui.base.MvpPresenter

interface IMonitoringPresenter: MvpPresenter<IMonitoringFragment> {

    fun handleMessage(intent: Intent)
}