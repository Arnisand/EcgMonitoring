package com.arnisand.ecgmonitoring.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arnisand.ecgmonitoring.di.component.ActivityComponent
import com.arnisand.ecgmonitoring.di.component.DaggerActivityComponent
import com.arnisand.ecgmonitoring.di.modul.ActivityModule
import com.arnisand.ecgmonitoring.ui.EcgApplication
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.jvmName

abstract class BaseActivity : AppCompatActivity(), MvpView {

    abstract val containerFragmentId: Int
    abstract val defaultFragment: KClass<out BaseFragment>

    lateinit var mActivityComponent: ActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent(EcgApplication.applicationComponent)
                .build()
    }

    fun addNewContent(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(fragment::class.jvmName)
                .replace(containerFragmentId, fragment)
                .commitAllowingStateLoss()
    }

    fun setDefaultFragment() {
        supportFragmentManager.beginTransaction()
                .replace(containerFragmentId, defaultFragment.createInstance())
                .commitAllowingStateLoss()
    }
}