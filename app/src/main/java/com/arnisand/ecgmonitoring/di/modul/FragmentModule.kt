package com.arnisand.ecgmonitoring.di.modul

import com.arnisand.ecgmonitoring.di.qualifier.PerFragment
import com.arnisand.ecgmonitoring.ui.base.BaseFragment
import com.arnisand.ecgmonitoring.ui.main.monitor.IMonitoringPresenter
import com.arnisand.ecgmonitoring.ui.main.monitor.MonitoringPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: BaseFragment) {

    @Provides
    @PerFragment
    fun provideMonitoringPresenter(presenter: MonitoringPresenter): IMonitoringPresenter = presenter

}