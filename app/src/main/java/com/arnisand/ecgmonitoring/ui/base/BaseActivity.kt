package com.arnisand.ecgmonitoring.ui.base

import android.support.v7.app.AppCompatActivity
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.jvmName

abstract class BaseActivity : AppCompatActivity(), MvpView {

    abstract val containerFragmentId: Int
    abstract val defaultFragment: KClass<out BaseFragment>

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