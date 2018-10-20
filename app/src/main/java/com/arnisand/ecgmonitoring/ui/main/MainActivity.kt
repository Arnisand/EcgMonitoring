package com.arnisand.ecgmonitoring.ui.main

import android.content.Intent
import android.os.Bundle
import com.arnisand.ecgmonitoring.background.SocketService
import com.arnisand.ecgmonitoring.ui.base.BaseActivity
import com.arnisand.ecgmonitoring.ui.base.BaseFragment
import com.arnisand.ecgmonitoring.ui.main.monitor.MonitoringFragment
import com.arnisand.egcmonitoring.R
import kotlin.reflect.KClass

class MainActivity : BaseActivity() {
    override val defaultFragment: KClass<out BaseFragment>
        get() = MonitoringFragment::class
    override val containerFragmentId: Int
        get() = R.id.container_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment()
        startService(Intent(applicationContext, SocketService::class.java))
    }
}
