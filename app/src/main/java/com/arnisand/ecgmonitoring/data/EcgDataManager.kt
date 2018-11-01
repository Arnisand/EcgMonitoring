package com.arnisand.ecgmonitoring.data

import com.arnisand.ecgmonitoring.data.api.ApiSocketHelper
import javax.inject.Inject

class EcgDataManager @Inject constructor(private val ecgSocketHelper: ApiSocketHelper): DataManager {

    override fun openSocketConnection() = ecgSocketHelper.openSocketConnection()

    override fun closeSocketConnection() = ecgSocketHelper.closeSocketConnection()

    override fun sendMessage(text: String) = ecgSocketHelper.sendMessage(text)
}