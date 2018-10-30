package com.arnisand.ecgmonitoring.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arnisand.ecgmonitoring.di.component.DaggerFragmentComponent
import com.arnisand.ecgmonitoring.di.component.FragmentComponent
import com.arnisand.ecgmonitoring.di.modul.FragmentModule
import com.arnisand.ecgmonitoring.ui.EcgApplication


abstract class BaseFragment : Fragment(), MvpView {

    lateinit var mFragmentComponent: FragmentComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .applicationComponent(EcgApplication.applicationComponent)
                .build()
    }

    fun addContentFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.addNewContent(fragment)
    }
}