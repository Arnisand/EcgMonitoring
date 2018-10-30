package com.arnisand.ecgmonitoring.ui.base

import com.arnisand.ecgmonitoring.data.DataManager
import javax.inject.Inject

abstract class BasePresenter<V : MvpView> : MvpPresenter<V> {
    @Inject
    lateinit var mDataManager: DataManager

    private var mMvpView: V? = null

    override fun attachView(mvpView: V) {
        this.mMvpView = mvpView
    }

    override fun detachView() {
        mMvpView = null
    }

    override fun getView(): V?  = mMvpView
}
