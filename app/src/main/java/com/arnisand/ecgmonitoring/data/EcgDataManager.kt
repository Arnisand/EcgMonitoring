package com.arnisand.ecgmonitoring.data

import com.arnisand.ecgmonitoring.background.SocketService
import com.arnisand.ecgmonitoring.data.api.ApiSocketHelper
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper

class EcgDataManager: DataManager {
    private val ecgSocketHelper: ApiSocketHelper = EcgApiSocketHelper()

    override fun openSocketConnection(token: String) = ecgSocketHelper.openSocketConnection(token)

    override fun closeSocketConnection() = ecgSocketHelper.closeSocketConnection()

    override fun sendMessage(text: String) = ecgSocketHelper.sendMessage(text)
}