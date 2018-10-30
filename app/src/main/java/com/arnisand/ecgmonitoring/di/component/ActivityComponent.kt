package com.arnisand.ecgmonitoring.di.component

import com.arnisand.ecgmonitoring.di.modul.ActivityModule
import com.arnisand.ecgmonitoring.di.qualifier.PerActivity
import com.arnisand.ecgmonitoring.ui.main.MainActivity
import dagger.Component

@PerActivity
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
}