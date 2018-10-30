package com.arnisand.ecgmonitoring.di.component

import android.content.Context
import com.arnisand.ecgmonitoring.data.DataManager
import com.arnisand.ecgmonitoring.di.modul.ApplicationModule
import com.arnisand.ecgmonitoring.di.qualifier.ApplicationContext
import com.arnisand.ecgmonitoring.ui.EcgApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun dataManager(): DataManager

    fun inject(ecgApplication: EcgApplication)
}