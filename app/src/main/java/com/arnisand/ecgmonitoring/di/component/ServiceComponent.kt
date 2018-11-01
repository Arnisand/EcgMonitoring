package com.arnisand.ecgmonitoring.di.component

import com.arnisand.ecgmonitoring.background.SocketService
import com.arnisand.ecgmonitoring.di.qualifier.PerService
import dagger.Component

@PerService
@Component(dependencies = [ApplicationComponent::class])
interface ServiceComponent {

    fun inject(socketService: SocketService)
}