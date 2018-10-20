package com.arnisand.ecgmonitoring.data.api

interface ApiSocketHelper {
    fun openSocketConnection(token: String)

    fun closeSocketConnection()

    fun sendMessage(text: String)
}