package com.arnisand.ecgmonitoring.ui.base

interface MvpPresenter<V : MvpView> {

    fun attachView(mvpView: V)
    fun detachView()
    fun getView(): V?
}