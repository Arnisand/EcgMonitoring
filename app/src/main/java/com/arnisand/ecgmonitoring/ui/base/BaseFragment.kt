package com.arnisand.ecgmonitoring.ui.base

import android.support.v4.app.Fragment


abstract class BaseFragment: Fragment(), MvpView {

    fun addContentFragment(fragment: BaseFragment) {
        (activity as? BaseActivity)?.addNewContent(fragment)
    }
}