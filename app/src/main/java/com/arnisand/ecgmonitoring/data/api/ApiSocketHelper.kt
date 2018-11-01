package com.arnisand.ecgmonitoring.data.api

interface ApiSocketHelper {
    fun openSocketConnection()

    fun closeSocketConnection()

    fun sendMessage(text: String)
}