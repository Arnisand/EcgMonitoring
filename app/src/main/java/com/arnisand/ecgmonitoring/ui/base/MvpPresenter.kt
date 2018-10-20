package com.arnisand.ecgmonitoring.ui.base

interface MvpPresenter {

    fun attachView()
    fun detachView()
    fun getView(): MvpView?
}