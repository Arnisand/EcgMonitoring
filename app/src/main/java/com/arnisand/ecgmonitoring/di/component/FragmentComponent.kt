package com.arnisand.ecgmonitoring.di.component

import com.arnisand.ecgmonitoring.di.modul.FragmentModule
import com.arnisand.ecgmonitoring.di.qualifier.PerFragment
import com.arnisand.ecgmonitoring.ui.main.monitor.MonitoringFragment
import dagger.Component

@PerFragment
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(monitoringFragment: MonitoringFragment)
}