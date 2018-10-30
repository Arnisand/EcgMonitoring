package com.arnisand.ecgmonitoring.ui

import android.app.Application
import com.arnisand.ecgmonitoring.data.DataManager
import com.arnisand.ecgmonitoring.di.component.ApplicationComponent
import com.arnisand.ecgmonitoring.di.component.DaggerApplicationComponent
import com.arnisand.ecgmonitoring.di.modul.ApplicationModule

class EcgApplication : Application() {

    private lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        mApplicationComponent.inject(this)

        instance = this
    }

    companion object {
        lateinit var instance: EcgApplication
        var mDataManager: DataManager? = null

        val applicationComponent: ApplicationComponent
            get() = instance.mApplicationComponent
    }
}