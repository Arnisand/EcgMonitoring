package com.arnisand.ecgmonitoring.di.modul

import android.content.Context
import com.arnisand.ecgmonitoring.data.DataManager
import com.arnisand.ecgmonitoring.data.EcgDataManager
import com.arnisand.ecgmonitoring.data.api.ApiSocketHelper
import com.arnisand.ecgmonitoring.data.api.EcgApiSocketHelper
import com.arnisand.ecgmonitoring.di.qualifier.ApplicationContext
import com.arnisand.ecgmonitoring.ui.EcgApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val ecgApplication: EcgApplication) {

    @Provides
    fun provideApplication() = ecgApplication

    @Provides
    @ApplicationContext
    fun provideContext(): Context = ecgApplication.applicationContext

    @Singleton
    @Provides
    fun provideDataManager(ecgDataManager: EcgDataManager): DataManager = ecgDataManager

    @Singleton
    @Provides
    fun provideApiSocketHelper(ecgApiSocketHelper: EcgApiSocketHelper): ApiSocketHelper = ecgApiSocketHelper
}