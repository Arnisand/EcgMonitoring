package com.arnisand.ecgmonitoring.ui

import android.app.Application
import com.arnisand.ecgmonitoring.data.DataManager
import com.arnisand.ecgmonitoring.data.EcgDataManager

class EcgApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mDataManager = EcgDataManager()
        instance = this
    }

    companion object {
        var instance: EcgApplication? = null
        var mDataManager: DataManager? = null
    }
}